import { useState, useEffect } from "react";
import {
    RiAdminFill,
    RiLogoutBoxLine,
    RiMenuLine,
    RiCloseLine,
    RiEditLine,
    RiUserLine,
    RiMailLine,
    RiPhoneLine,
    RiCalendarLine,
    RiShieldLine,
    RiSaveLine,
    RiCloseCircleLine,
    RiInformationLine,
    RiShareLine,
    RiMore2Line
} from "react-icons/ri";
import { adminNavItems } from "../../../constants/data";
import { Link, useNavigate } from "react-router-dom";

function Navbar({ activeItem, setActiveItem, email, logout }) {
    const [isMobile, setIsMobile] = useState(false);
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [showProfile, setShowProfile] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();

    const [userProfile, setUserProfile] = useState({
        name: "Admin User",
        email: email || "admin@example.com",
        phone: "+1 (555) 123-4567",
        role: "Administrator",
        joinDate: "January 15, 2024",
        department: "IT Management",
        bio: "System Administrator responsible for managing user accounts, security, and system operations.",
        lastUpdated: new Date().toLocaleDateString()
    });

    const [editableProfile, setEditableProfile] = useState({ ...userProfile });

    useEffect(() => {
        const handleResize = () => {
            const mobileView = window.innerWidth < 768;
            setIsMobile(mobileView);
            if (!mobileView) {
                setSidebarOpen(true);
            } else {
                setSidebarOpen(false);
            }
        };

        handleResize();
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    useEffect(() => {
        setUserProfile(prev => ({ ...prev, email: email || "admin@example.com" }));
        setEditableProfile(prev => ({ ...prev, email: email || "admin@example.com" }));
    }, [email]);

    const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

    const handleProfileClick = () => {
        setShowProfile(true);
        setIsEditing(false);
    };

    const handleSaveProfile = () => {
        const updatedProfile = {
            ...editableProfile,
            lastUpdated: new Date().toLocaleDateString()
        };
        setUserProfile(updatedProfile);
        setIsEditing(false);
    };

    const handleCancelEdit = () => {
        setEditableProfile({ ...userProfile });
        setIsEditing(false);
    };

    const handleInputChange = (field, value) => {
        setEditableProfile(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const closeModal = () => {
        setShowProfile(false);
        setIsEditing(false);
        setEditableProfile({ ...userProfile });
    };

    // Handle navigation without triggering on hover
    const handleNavigation = (item) => {
        setActiveItem(item.label);
        if (isMobile) setSidebarOpen(false);
        navigate(item.path);
    };

    return (
        <>
            {/* Mobile Header */}
            {isMobile && (
                <header className="fixed top-0 left-0 right-0 bg-gray-800 border-b border-gray-700 z-50 h-16">
                    <div className="flex items-center justify-between px-4 h-full">
                        <h1 className='text-center font-bold text-xl text-white'>
                            CodeScribe
                            <span className='ml-1 text-transparent font-bold bg-clip-text bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400'>
                                AI
                            </span>
                        </h1>
                        <button
                            onClick={toggleSidebar}
                            className="p-2 rounded-md bg-gray-700 text-white hover:bg-gray-600 transition-colors focus:outline-none focus:ring-2 focus:ring-purple-500"
                        >
                            {sidebarOpen ? <RiCloseLine className="text-xl" /> : <RiMenuLine className="text-xl" />}
                        </button>
                    </div>
                </header>
            )}

            {/* Overlay for Sidebar */}
            {isMobile && sidebarOpen && (
                <div
                    className="fixed inset-0 bg-black/60 backdrop-blur-sm z-40"
                    onClick={toggleSidebar}
                />
            )}

            {/* profile model */}
            {showProfile && (
                <div
                    className="fixed inset-0 bg-black/60 backdrop-blur-sm z-[60] flex items-center justify-center p-4"
                    onClick={(e) => e.target === e.currentTarget && closeModal()}
                >
                    <div className="bg-gray-800 rounded-2xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-hidden border border-gray-700 transform transition-all duration-300 ease-out">
                        {/* Modal Header */}
                        <div className="relative p-6 bg-gradient-to-r from-purple-600 via-pink-500 to-rose-500 rounded-t-2xl">
                            <button
                                onClick={closeModal}
                                className="absolute top-4 right-4 text-white hover:text-gray-200 text-xl transition-transform hover:scale-110"
                                aria-label="Close modal"
                            >
                                <RiCloseLine />
                            </button>

                            {/* Profile Image */}
                            <div className="flex flex-col items-center">
                                <div className="w-24 h-24 rounded-full bg-white/20 flex items-center justify-center text-white mb-4 backdrop-blur-sm border-2 border-white/30 shadow-lg">
                                    <RiAdminFill className="text-4xl" />
                                </div>
                                <h2 className="text-2xl font-bold text-white text-center">{userProfile.name}</h2>
                                <div className="mt-1 px-3 py-1 bg-white/10 rounded-full backdrop-blur-sm">
                                    <p className="text-purple-100 text-sm font-medium">{userProfile.role}</p>
                                </div>
                            </div>
                        </div>

                        {/* Modal Content */}
                        <div className="p-6 overflow-y-auto max-h-[60vh] scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-800">
                            {/* Action Buttons */}
                            <div className="flex justify-between mb-6">
                                {!isEditing ? (
                                    <button
                                        onClick={() => setIsEditing(true)}
                                        className="flex items-center px-4 py-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white rounded-lg hover:opacity-90 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 focus:ring-offset-gray-800 shadow-md hover:shadow-lg"
                                    >
                                        <RiEditLine className="mr-2" />
                                        Edit Profile
                                    </button>
                                ) : (
                                    <div className="flex gap-3 w-full">
                                        <button
                                            onClick={handleSaveProfile}
                                            className="flex items-center justify-center px-4 py-2 bg-gradient-to-r from-green-600 to-emerald-500 text-white rounded-lg hover:opacity-90 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 focus:ring-offset-gray-800 shadow-md flex-1"
                                        >
                                            <RiSaveLine className="mr-2" />
                                            Save Changes
                                        </button>
                                        <button
                                            onClick={handleCancelEdit}
                                            className="flex items-center justify-center px-4 py-2 bg-gradient-to-r from-gray-600 to-gray-500 text-white rounded-lg hover:opacity-90 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 focus:ring-offset-gray-800 shadow-md flex-1"
                                        >
                                            <RiCloseCircleLine className="mr-2" />
                                            Cancel
                                        </button>
                                    </div>
                                )}
                            </div>

                            {/* Profile Details */}
                            <div className="space-y-5">
                                {/* Name */}
                                <div className="flex items-start gap-3 group">
                                    <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                        <RiUserLine className="text-purple-400 text-lg" />
                                    </div>
                                    <div className="flex-1">
                                        <label className="block text-sm font-medium text-gray-300 mb-1">Full Name</label>
                                        {isEditing ? (
                                            <input
                                                type="text"
                                                value={editableProfile.name}
                                                onChange={(e) => handleInputChange('name', e.target.value)}
                                                className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-purple-500 focus:ring-1 focus:ring-purple-500 focus:outline-none transition-all duration-200"
                                                placeholder="Enter your full name"
                                            />
                                        ) : (
                                            <p className="text-white font-medium">{userProfile.name}</p>
                                        )}
                                    </div>
                                </div>

                                {/* Email */}
                                <div className="flex items-start gap-3 group">
                                    <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                        <RiMailLine className="text-purple-400 text-lg" />
                                    </div>
                                    <div className="flex-1">
                                        <label className="block text-sm font-medium text-gray-300 mb-1">Email</label>
                                        {isEditing ? (
                                            <input
                                                type="email"
                                                value={editableProfile.email}
                                                onChange={(e) => handleInputChange('email', e.target.value)}
                                                className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-purple-500 focus:ring-1 focus:ring-purple-500 focus:outline-none transition-all duration-200"
                                                placeholder="Enter your email"
                                            />
                                        ) : (
                                            <p className="text-white font-medium break-all">{userProfile.email}</p>
                                        )}
                                    </div>
                                </div>

                                {/* Phone */}
                                <div className="flex items-start gap-3 group">
                                    <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                        <RiPhoneLine className="text-purple-400 text-lg" />
                                    </div>
                                    <div className="flex-1">
                                        <label className="block text-sm font-medium text-gray-300 mb-1">Phone</label>
                                        {isEditing ? (
                                            <input
                                                type="tel"
                                                value={editableProfile.phone}
                                                onChange={(e) => handleInputChange('phone', e.target.value)}
                                                className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-purple-500 focus:ring-1 focus:ring-purple-500 focus:outline-none transition-all duration-200"
                                                placeholder="Enter your phone number"
                                            />
                                        ) : (
                                            <p className="text-white font-medium">{userProfile.phone}</p>
                                        )}
                                    </div>
                                </div>

                                {/* Department */}
                                <div className="flex items-start gap-3 group">
                                    <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                        <RiShieldLine className="text-purple-400 text-lg" />
                                    </div>
                                    <div className="flex-1">
                                        <label className="block text-sm font-medium text-gray-300 mb-1">Department</label>
                                        {isEditing ? (
                                            <select
                                                value={editableProfile.department}
                                                onChange={(e) => handleInputChange('department', e.target.value)}
                                                className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-purple-500 focus:ring-1 focus:ring-purple-500 focus:outline-none transition-all duration-200"
                                            >
                                                <option value="Engineering">Engineering</option>
                                                <option value="Design">Design</option>
                                                <option value="Marketing">Marketing</option>
                                                <option value="Sales">Sales</option>
                                                <option value="HR">Human Resources</option>
                                            </select>
                                        ) : (
                                            <p className="text-white font-medium">{userProfile.department}</p>
                                        )}
                                    </div>
                                </div>

                                {/* Join Date */}
                                <div className="flex items-start gap-3 group">
                                    <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                        <RiCalendarLine className="text-purple-400 text-lg" />
                                    </div>
                                    <div className="flex-1">
                                        <label className="block text-sm font-medium text-gray-300 mb-1">Join Date</label>
                                        <p className="text-white font-medium">{userProfile.joinDate}</p>
                                    </div>
                                </div>

                                {/* Bio */}
                                <div className="space-y-2 group">
                                    <div className="flex items-center gap-3">
                                        <div className="p-2 bg-purple-500/10 rounded-lg group-hover:bg-purple-500/20 transition-colors duration-200">
                                            <RiInformationLine className="text-purple-400 text-lg" />
                                        </div>
                                        <label className="block text-sm font-medium text-gray-300">Bio</label>
                                    </div>
                                    {isEditing ? (
                                        <textarea
                                            value={editableProfile.bio}
                                            onChange={(e) => handleInputChange('bio', e.target.value)}
                                            rows={3}
                                            className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-purple-500 focus:ring-1 focus:ring-purple-500 focus:outline-none resize-none transition-all duration-200"
                                            placeholder="Tell us about yourself..."
                                        />
                                    ) : (
                                        <p className="text-gray-300 text-sm leading-relaxed pl-11">{userProfile.bio || "No bio provided"}</p>
                                    )}
                                </div>
                            </div>
                        </div>

                        {/* Footer */}
                        <div className="px-6 py-4 border-t border-gray-700 bg-gray-800/50 backdrop-blur-sm">
                            <div className="flex justify-between items-center text-sm text-gray-400">
                                <span>Last updated: {userProfile.lastUpdated}</span>
                                <div className="flex gap-2">
                                    <button
                                        className="p-2 text-gray-400 hover:text-purple-400 transition-colors duration-200 hover:scale-110"
                                        aria-label="Share profile"
                                    >
                                        <RiShareLine />
                                    </button>
                                    <button
                                        className="p-2 text-gray-400 hover:text-purple-400 transition-colors duration-200 hover:scale-110"
                                        aria-label="More options"
                                    >
                                        <RiMore2Line />
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* Sidebar */}
            <aside className={`fixed h-screen bg-gray-800 border-r border-gray-700 z-50 transition-all duration-300 ease-in-out ${isMobile
                ? (sidebarOpen ? 'w-64 left-0' : '-left-64 w-64')
                : 'left-0 w-64'
                }`}>
                <div className="flex flex-col h-full">
                    {/* Sidebar Header */}
                    <div className="flex flex-col p-6 border-b border-gray-700">
                        <h1 className='font-bold text-2xl text-white'>
                            CodeScribe
                            <span className='ml-1 text-transparent font-bold bg-clip-text bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400'>
                                AI
                            </span>
                        </h1>
                        <div className="mt-2 text-xs text-gray-400 flex items-center">
                            <RiAdminFill className="mr-1" />
                            <span>ADMIN PANEL</span>
                        </div>
                    </div>

                    {/* Navigation Items */}
                    <nav className="flex-1 overflow-y-auto px-4 py-6 space-y-2">
                        {adminNavItems.map((item) => (
                            <button
                                key={item.label}
                                onClick={() => handleNavigation(item)}
                                className={`flex items-center w-full px-4 py-3 rounded-lg transition-all duration-200 text-left ${activeItem === item.label
                                    ? 'bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-lg'
                                    : 'text-gray-300 hover:bg-gray-700 hover:text-white'
                                    }`}
                            >
                                <span className="mr-3">{item.icon}</span>
                                <span className="font-medium">{item.label}</span>
                            </button>
                        ))}
                    </nav>

                    {/* Sidebar Footer / Profile */}
                    <div className="px-4 py-6 border-t border-gray-700">
                        <div
                            className="flex items-center mb-4 cursor-pointer hover:bg-gray-700 rounded-lg p-2 transition-colors"
                            onClick={handleProfileClick}
                        >
                            <div className="w-10 h-10 rounded-full bg-gradient-to-r from-purple-500 to-pink-500 flex items-center justify-center text-white">
                                <RiAdminFill className="text-xl" />
                            </div>
                            <div className="ml-3 flex-1 min-w-0">
                                <p className="text-sm font-medium text-white">Admin User</p>
                                <p className="text-xs text-gray-400 truncate">{email}</p>
                            </div>
                        </div>

                        <button
                            onClick={() => {
                                setActiveItem("Logout");
                                logout();
                            }}
                            className={`flex items-center w-full px-4 py-3 rounded-lg transition-all duration-200 text-left ${activeItem === 'Logout'
                                ? 'bg-red-600 text-white'
                                : 'text-gray-300 hover:bg-red-600 hover:text-white'
                                }`}
                        >
                            <RiLogoutBoxLine className="text-xl mr-3" />
                            <span className="font-medium">Log out</span>
                        </button>
                    </div>
                </div>
            </aside>
        </>
    );
}

export default Navbar;