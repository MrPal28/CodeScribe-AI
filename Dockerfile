# Use official Python Alpine image
FROM python:3.10-alpine3.16

# Set working directory
WORKDIR /app

# Copy all files to container
COPY . /app

# Install Python dependencies
RUN pip install --upgrade pip && pip install -r requirements.txt

# Expose the port used by the Flask app
EXPOSE 8000

# Use JSON form of CMD to avoid shell signal issues
CMD ["python", "app.py"]
