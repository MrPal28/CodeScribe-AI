import { useState, useEffect, useRef } from "react";
import {
    RiMenuLine,
    RiCloseLine,
    RiUserLine,
    RiSearchLine,
    RiAddLine,
    RiCloseFill,
    RiUploadLine,
    RiLogoutBoxLine
} from "react-icons/ri";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../../context/AuthContext";
import toast from "react-hot-toast";
import { userNavItems } from "../../../constants/data";

function Navbar({ activeItem, setActiveItem, email }) {
    const [isMobile, setIsMobile] = useState(false);
    const [showSearchModal, setShowSearchModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showProfileDropdown, setShowProfileDropdown] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");
    const [blogData, setBlogData] = useState({
        title: "",
        content: "",
        image: null,
        previewImage: null
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isLoggingOut, setIsLoggingOut] = useState(false);
    const dropdownRef = useRef(null);

    const navigate = useNavigate();
    const location = useLocation();
    const { user, allUsers, fetchAllUsers, logout, uploadBlog, fetchUserDetails } = useAuth();

    useEffect(() => {
        fetchAllUsers();
    }, [user, location, fetchAllUsers]);

    useEffect(() => {
        const handleResize = () => {
            setIsMobile(window.innerWidth < 768);
        };

        handleResize();
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowProfileDropdown(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleNavigation = (item) => {
        setActiveItem(item.label);
        setShowProfileDropdown(false);
        if (item.label === "Search") setShowSearchModal(true);
        else if (item.label === "Create") setShowCreateModal(true);
        else if (item.path) navigate(item.path);
    };

    const handleLogout = async () => {
        setIsLoggingOut(true);
        try {
            await logout();
            navigate('/login');
        } catch (err) {
            console.error('Error while logging out:', err);
            toast.error("Something went wrong during logout");
        } finally {
            setIsLoggingOut(false);
        }
    };

    const handleProfileClick = () => {
        setShowProfileDropdown(!showProfileDropdown);
    };

    const handleProfileNavigation = () => {
        setActiveItem('Profile');
        navigate('/user/profile');
        setShowProfileDropdown(false);
    };

    const handleSearch = (e) => {
        setSearchQuery(e.target.value);
    };

    const filteredUsers = allUsers?.filter(user =>
        `${user.firstname || ''} ${user.lastName || ''}`.toLowerCase().includes(searchQuery.toLowerCase()) ||
        user.email?.toLowerCase().includes(searchQuery.toLowerCase())
    ) || [];

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setBlogData(prev => ({
                    ...prev,
                    image: file,
                    previewImage: reader.result
                }));
            };
            reader.readAsDataURL(file);
        }
    };

    const removeImage = () => {
        setBlogData(prev => ({
            ...prev,
            image: null,
            previewImage: null
        }));
    };

    const handleBlogSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            await uploadBlog(
                {title: blogData.title, content: blogData.content},
                blogData.image
            );

            setShowCreateModal(false);
            setBlogData({
                title: "",
                content: "",
                image: null,
                previewImage: null
            });

            await fetchUserDetails();
        } catch (error) {
            toast.error("Failed upload blog")
            console.error("Error creating blog:", error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const closeModal = () => {
        setShowSearchModal(false);
        setShowCreateModal(false);
        setSearchQuery("");
        setBlogData({
            title: "",
            content: "",
            image: null,
            previewImage: null
        });
        navigate(location);
    };

    const handleUserClick = (user) => {
        closeModal();
        navigate(`/user/profile/${user.id}`, {
            state: { user },
            replace: false
        });
    };

    const handleShowProfileImage = () => {
        if (user?.profileImage?.url) {
            return (
                <img
                    src={user.profileImage.url}
                    alt="Profile"
                    className="w-8 h-8 rounded-full object-cover ring-2 ring-purple-500"
                    onError={(e) => (e.target.style.display = 'none')}
                />
            );
        }
        return (
            <div className="w-8 h-8 rounded-full bg-gradient-to-r from-purple-600 to-pink-500 flex items-center justify-center ring-2 ring-purple-500">
                <RiUserLine className="text-lg text-white" />
            </div>
        );
    };

    return (
        <>
            {/* Mobile Top Header */}
            {isMobile && (
                <header className="fixed top-0 left-0 right-0 bg-gray-950 border-b border-gray-800 z-50 h-14 flex items-center justify-center px-4">
                    <h1 className="font-semibold text-xl text-white tracking-tight">
                        CodeScribe
                        <span className="ml-1 text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 to-purple-400">
                            AI
                        </span>
                    </h1>
                </header>
            )}

            {/* Search Modal */}
            {showSearchModal && (
                <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
                    <div className="bg-gray-800 rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-hidden border border-gray-700 transform transition-all duration-300">
                        <div className="p-4 border-b border-gray-700 flex justify-between items-center">
                            <h3 className="text-base font-semibold text-white">Search Users</h3>
                            <button
                                onClick={closeModal}
                                className="text-gray-300 hover:text-white transition-colors focus:outline-none"
                                aria-label="Close search modal"
                            >
                                <RiCloseFill className="text-xl" />
                            </button>
                        </div>
                        <div className="p-4">
                            <div className="relative mb-4">
                                <RiSearchLine className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 text-lg" />
                                <input
                                    type="text"
                                    placeholder="Search by name or email"
                                    className="w-full pl-10 pr-3 py-2 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 border border-gray-600 transition-all text-sm"
                                    value={searchQuery}
                                    onChange={handleSearch}
                                    autoFocus
                                    aria-label="Search users"
                                />
                            </div>
                            <div className="space-y-2 max-h-[60vh] overflow-y-auto custom-scrollbar">
                                {filteredUsers.length > 0 ? (
                                    filteredUsers.map(user => (
                                        <div
                                            key={user.id}
                                            onClick={() => handleUserClick(user)}
                                            className="p-3 bg-gray-700 rounded-lg hover:bg-gray-600 transition-colors cursor-pointer"
                                            role="button"
                                            tabIndex={0}
                                            onKeyDown={(e) => e.key === 'Enter' && handleUserClick(user)}
                                        >
                                            <div className="flex items-center space-x-3">
                                                {user?.profileImage?.url ? (
                                                    <img
                                                        src={user.profileImage.url}
                                                        alt={`${user?.firstName || ''} ${user?.lastName || ''}`.trim() || 'User'}
                                                        className="w-10 h-10 rounded-full object-cover ring-1 ring-gray-500"
                                                        onError={(e) => (e.target.style.display = 'none')}
                                                    />
                                                ) : (
                                                    <div className="w-10 h-10 rounded-full bg-gradient-to-r from-purple-600 to-pink-500 flex items-center justify-center text-white ring-1 ring-gray-500">
                                                        {user?.firstName?.[0]?.toUpperCase() || user?.lastName?.[0]?.toUpperCase() || (
                                                            <RiUserLine className="text-xl" />
                                                        )}
                                                    </div>
                                                )}
                                                <div className="flex-1 min-w-0">
                                                    <p className="font-medium text-white truncate text-sm">
                                                        {user.firstName} {user.lastName}
                                                    </p>
                                                    <p className="text-xs text-gray-400 truncate">{user.email}</p>
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <div className="text-center py-8">
                                        <RiSearchLine className="mx-auto text-3xl text-gray-500 mb-3" />
                                        <p className="text-gray-400 text-sm">No users found</p>
                                        {searchQuery && (
                                            <p className="text-xs text-gray-500 mt-2">Try different search terms</p>
                                        )}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* Create Blog Modal */}
            {showCreateModal && (
                <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
                    <div className="bg-gray-800 rounded-xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-hidden border border-gray-700 transform transition-all duration-300">
                        <div className="p-4 border-b border-gray-700 flex justify-between items-center">
                            <h3 className="text-base font-semibold text-white">Create New Blog Post</h3>
                            <button
                                onClick={closeModal}
                                className="text-gray-300 hover:text-white transition-colors focus:outline-none"
                                aria-label="Close create blog modal"
                            >
                                <RiCloseFill className="text-xl" />
                            </button>
                        </div>
                        <form onSubmit={handleBlogSubmit} className="p-4">
                            <div className="mb-4">
                                <label htmlFor="title" className="block text-sm font-medium text-gray-300 mb-2">
                                    Title
                                </label>
                                <input
                                    type="text"
                                    id="title"
                                    className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 border border-gray-600 transition-all text-sm"
                                    value={blogData.title}
                                    onChange={(e) => setBlogData(prev => ({
                                        ...prev,
                                        title: e.target.value
                                    }))}
                                    required
                                    aria-required="true"
                                />
                            </div>

                            <div className="mb-4">
                                <label htmlFor="content" className="block text-sm font-medium text-gray-300 mb-2">
                                    Content
                                </label>
                                <textarea
                                    id="content"
                                    rows={5}
                                    className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 border border-gray-600 transition-all text-sm"
                                    value={blogData.content}
                                    onChange={(e) => setBlogData(prev => ({
                                        ...prev,
                                        content: e.target.value
                                    }))}
                                    required
                                    aria-required="true"
                                />
                            </div>

                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-300 mb-2">
                                    Featured Image
                                </label>
                                {blogData.previewImage ? (
                                    <div className="relative mb-3">
                                        <img
                                            src={blogData.previewImage}
                                            alt="Preview"
                                            className="w-full h-48 object-cover rounded-lg"
                                        />
                                        <button
                                            type="button"
                                            onClick={removeImage}
                                            className="absolute top-2 right-2 bg-gray-900/80 text-white p-1.5 rounded-full hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-purple-500"
                                            aria-label="Remove image"
                                        >
                                            <RiCloseFill className="w-5 h-5" />
                                        </button>
                                    </div>
                                ) : (
                                    <div className="flex items-center justify-center w-full">
                                        <label className="flex flex-col items-center justify-center w-full h-28 border-2 border-gray-600 border-dashed rounded-lg cursor-pointer bg-gray-700 hover:bg-gray-600 transition-colors">
                                            <div className="flex flex-col items-center justify-center pt-4 pb-4">
                                                <RiUploadLine className="w-8 h-8 text-gray-400 mb-2" />
                                                <p className="text-xs text-gray-400">Upload an image</p>
                                            </div>
                                            <input
                                                type="file"
                                                className="hidden"
                                                accept="image/*"
                                                onChange={handleImageChange}
                                                aria-label="Upload featured image"
                                            />
                                        </label>
                                    </div>
                                )}
                            </div>

                            <button
                                type="submit"
                                className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white py-2.5 px-4 rounded-lg font-medium hover:opacity-90 transition-opacity flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed focus:outline-none focus:ring-2 focus:ring-purple-500 text-sm"
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? (
                                    <>
                                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                                        Publishing...
                                    </>
                                ) : (
                                    <>
                                        <RiAddLine className="mr-2 text-lg" />
                                        Publish Blog
                                    </>
                                )}
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Desktop Sidebar */}
            {!isMobile && (
                <aside
                    className="fixed top-0 left-0 h-screen bg-gray-950 border-r border-gray-800 z-50 flex flex-col w-56 lg:w-64"
                >
                    <div className="flex flex-col p-4 border-b border-gray-800 flex-shrink-0">
                        <h1 className="font-semibold text-xl text-white tracking-tight">
                            CodeScribe
                            <span className="ml-1 text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 to-purple-400">
                                AI
                            </span>
                        </h1>
                    </div>

                    <nav className="flex-1 px-4 py-3 overflow-y-auto custom-scrollbar">
                        <div className="space-y-2">
                            {userNavItems.map((item) => (
                                <button
                                    key={item.label}
                                    onClick={() => handleNavigation(item)}
                                    className={`flex items-center w-full px-3 py-2.5 rounded-lg transition-all duration-200 text-left ${activeItem === item.label
                                        ? 'bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-md'
                                        : 'text-gray-200 hover:bg-gray-800 hover:text-white'
                                        } focus:outline-none focus:ring-2 focus:ring-purple-500`}
                                    aria-current={activeItem === item.label ? 'page' : undefined}
                                >
                                    <span className="mr-3 flex-shrink-0 text-lg">{item.icon}</span>
                                    <span className="font-medium text-sm truncate">{item.label}</span>
                                </button>
                            ))}
                        </div>
                    </nav>

                    <div className="p-4 border-t border-gray-800 flex-shrink-0 relative" ref={dropdownRef}>
                        <div
                            className="flex items-center cursor-pointer hover:bg-gray-800 rounded-lg p-2 transition-colors"
                            onClick={handleProfileClick}
                            role="button"
                            aria-haspopup="true"
                            aria-expanded={showProfileDropdown}
                        >
                            <div className="flex-shrink-0">
                                {handleShowProfileImage()}
                            </div>
                            <div className="ml-3 flex-1 min-w-0">
                                <p className="text-sm font-medium text-white truncate">
                                    {`${user?.firstname || ''} ${user?.lastName || ''}`.trim() || 'User'}
                                </p>
                                <p className="text-xs text-gray-400 truncate">{email}</p>
                            </div>
                        </div>
                        {showProfileDropdown && (
                            <div className="absolute bottom-16 left-4 right-4 bg-gray-800 rounded-lg shadow-xl border border-gray-700 z-10 overflow-hidden">
                                <button
                                    onClick={handleProfileNavigation}
                                    className="w-full px-4 py-2.5 text-left text-gray-200 hover:bg-gray-700 hover:text-white transition-colors focus:outline-none focus:bg-gray-700 text-sm"
                                    aria-label="View profile"
                                >
                                    Profile
                                </button>
                                <button
                                    onClick={handleLogout}
                                    disabled={isLoggingOut}
                                    className="w-full px-4 py-2.5 text-left text-gray-200 hover:bg-red-600 hover:text-white transition-colors flex items-center disabled:opacity-50 disabled:cursor-not-allowed focus:outline-none focus:bg-red-600 text-sm"
                                    aria-label="Log out"
                                >
                                    {isLoggingOut ? (
                                        <>
                                            <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-current mr-2"></div>
                                            Logging out...
                                        </>
                                    ) : (
                                        <>
                                            <RiLogoutBoxLine className="mr-2 text-lg" />
                                            Log out
                                        </>
                                    )}
                                </button>
                            </div>
                        )}
                    </div>
                </aside>
            )}

            {/* Mobile Bottom Navbar */}
            {isMobile && (
                <nav className="fixed bottom-0 left-0 right-0 bg-gray-950/95 backdrop-blur-sm border-t border-gray-800 z-50 flex justify-around items-center h-16 shadow-lg">
                    {userNavItems
                        .filter(item => item.label !== 'Profile')
                        .map((item) => (
                            <button
                                key={item.label}
                                onClick={() => handleNavigation(item)}
                                className={`flex items-center justify-center w-16 h-16 transition-colors duration-200 focus:outline-none ${activeItem === item.label
                                        ? 'text-purple-400'
                                        : 'text-gray-400 hover:text-purple-400'
                                    }`}
                                aria-label={item.label}
                            >
                                <span className="text-2xl">{item.icon}</span>
                            </button>
                        ))}
                    <div className="relative" ref={dropdownRef}>
                        <button
                            onClick={handleProfileClick}
                            className={`flex items-center justify-center w-16 h-16 transition-colors duration-200 focus:outline-none ${activeItem === 'Profile'
                                    ? 'text-purple-400'
                                    : 'text-gray-400 hover:text-purple-400'
                                }`}
                            aria-label="Profile"
                            aria-haspopup="true"
                            aria-expanded={showProfileDropdown}
                        >
                            {handleShowProfileImage()}
                        </button>
                        {showProfileDropdown && (
                            <div className="absolute bottom-20 left-1/2 transform -translate-x-1/2 w-48 bg-gray-800 rounded-lg shadow-xl border border-gray-700 z-10 overflow-hidden">
                                <button
                                    onClick={handleProfileNavigation}
                                    className="w-full px-4 py-2.5 text-left text-gray-200 hover:bg-gray-700 hover:text-purple-400 transition-colors focus:outline-none text-sm"
                                >
                                    Profile
                                </button>
                                <button
                                    onClick={handleLogout}
                                    disabled={isLoggingOut}
                                    className="w-full px-4 py-2.5 text-left text-gray-200 hover:bg-red-600 hover:text-white transition-colors flex items-center disabled:opacity-50 disabled:cursor-not-allowed focus:outline-none text-sm"
                                >
                                    {isLoggingOut ? (
                                        <>
                                            <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-current mr-2"></div>
                                            Logging out...
                                        </>
                                    ) : (
                                        <>
                                            <RiLogoutBoxLine className="mr-2 text-lg" />
                                            Log out
                                        </>
                                    )}
                                </button>
                            </div>
                        )}
                    </div>
                </nav>
            )}

            <style jsx>{`
                .custom-scrollbar::-webkit-scrollbar {
                    width: 6px;
                }

                .custom-scrollbar::-webkit-scrollbar-track {
                    background: transparent;
                }

                .custom-scrollbar::-webkit-scrollbar-thumb {
                    background: #4B5563;
                    border-radius: 3px;
                }

                .custom-scrollbar::-webkit-scrollbar-thumb:hover {
                    background: #374151;
                }

                aside {
                    max-height: 100vh;
                    overflow: hidden;
                }

                .modal-enter {
                    opacity: 0;
                    transform: scale(0.95);
                }
                .modal-enter-active {
                    opacity: 1;
                    transform: scale(1);
                    transition: opacity 300ms, transform 300ms;
                }
                .modal-exit {
                    opacity: 1;
                    transform: scale(1);
                }
                .modal-exit-active {
                    opacity: 0;
                    transform: scale(0.95);
                    transition: opacity 300ms, transform 300ms;
                }

                @media (max-width: 767px) {
                    .modal-enter {
                        transform: translateY(20px);
                    }
                    .modal-enter-active {
                        transform: translateY(0);
                    }
                    .modal-exit {
                        transform: translateY(0);
                    }
                    .modal-exit-active {
                        transform: translateY(20px);
                    }
                }

                @media (min-width: 768px) {
                    aside {
                        width: 224px;
                    }
                }

                @media (min-width: 1024px) {
                    aside {
                        width: 256px;
                    }
                }
            `}</style>
        </>
    );
}

export default Navbar;