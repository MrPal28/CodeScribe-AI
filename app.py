from flask import Flask, request, jsonify
import google.generativeai as genai
import re
import os
from dotenv import load_dotenv
import logging

load_dotenv()

app = Flask(__name__)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Get API key securely
API_KEY = os.getenv("GEMINI_API_KEY")
if not API_KEY:
    logger.error("GEMINI_API_KEY is not set in environment variables")
    raise RuntimeError("GEMINI_API_KEY is not set in environment variables")

genai.configure(api_key=API_KEY)

default_prompt = (
    "Please analyze the following paragraph and detect any offensive, inappropriate, or slang words. "
    "For each detected word, provide:\n"
    "1. The word\n"
    "2. A short explanation of why it's inappropriate\n"
    "Do not suggest any alternatives.\n"
    "If no such content is found, simply reply with: 'All clear. No inappropriate content found.'\n\n"
    "Here is the paragraph:\n\n"
)

@app.route('/')
def home():
    return jsonify({"message": "OK"}), 200

@app.route('/chat', methods=['POST'])
def chat():
    data = request.get_json() or {}
    user_input = data.get("prompt", "").strip()
    if not user_input:
        return jsonify({"error": "Prompt is required"}), 400

    try:
        model = genai.GenerativeModel("gemini-1.5-flash")
        full_prompt = default_prompt + user_input
        response = model.generate_content(full_prompt)
        result_text = response.text.strip()

        is_inappropriate = not ("all clear" in result_text.lower() or "no inappropriate content" in result_text.lower())
        words_info = []

        if is_inappropriate:
            matches = re.findall(
                r"(?i)(?:word)[:\-]?\s*['\"]?([\w\-]+)['\"]?\s*[\n\-]*.*?explanation[:\-]?\s*(.*?)(?:\n|$)",
                result_text
            )
            for word, explanation in matches:
                words_info.append({
                    "word": word.strip(),
                    "explanation": explanation.strip()
                })

        return jsonify({
            "input": user_input,
            "moderation_result": result_text,
            "is_inappropriate": is_inappropriate,
            "flagged_words": words_info
        })

    except Exception as e:
        logger.error(f"Error processing chat request: {e}")
        return jsonify({"error": "Internal server error"}), 500


if __name__ == '__main__':
    port = int(os.getenv("PORT", 8000))
    app.run(host='0.0.0.0', port=port, debug=False)
