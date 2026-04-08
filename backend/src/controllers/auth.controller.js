const asyncHandler = require("../utils/asyncHandler");
const ApiResponse = require("../utils/apiResponse");
const authService = require("../services/auth.service");
const User = require("../models/user.model");
const ApiError = require("../utils/apiError");

const register = asyncHandler(async (req, res) => {
  const { name, email, password } = req.body;
  if (!name || !email || !password) {
    throw new ApiError(400, "name, email and password are required");
  }

  const user = await authService.registerUser(req.body);
  return res.status(201).json(new ApiResponse(201, user, "User registered successfully"));
});

const login = asyncHandler(async (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) {
    throw new ApiError(400, "email and password are required");
  }

  const result = await authService.loginUser(email, password);
  return res.status(200).json(new ApiResponse(200, result, "Login successful"));
});

const getMe = asyncHandler(async (req, res) => {
  return res.status(200).json(new ApiResponse(200, req.user, "User profile fetched"));
});

const updateProfile = asyncHandler(async (req, res) => {
  const { name, avatar } = req.body;
  const user = await User.findByIdAndUpdate(
    req.user._id,
    { $set: { name, avatar } },
    { new: true }
  ).select("-password");
  
  return res.status(200).json(new ApiResponse(200, user, "Profile updated"));
});

module.exports = {
  register,
  login,
  getMe,
  updateProfile,
};
