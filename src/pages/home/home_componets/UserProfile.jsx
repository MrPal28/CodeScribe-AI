import { useEffect } from 'react';
import { ArrowLeftIcon } from '@heroicons/react/24/solid';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

function UserProfile() {
    const navigate = useNavigate();

    useEffect(() => {
        // Show error toast and navigate after a delay
        const timer = setTimeout(() => {
            toast.error("Cannot reach the server");
            navigate('/user/home');
        }, 0); // Immediate execution after render

        return () => clearTimeout(timer); // Clean up timer
    }, [navigate]);

    return null; // This component doesn't render anything visible
}

export default UserProfile;