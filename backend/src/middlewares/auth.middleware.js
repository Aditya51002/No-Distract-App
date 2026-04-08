const jwt = require("jsonwebtoken");
const User = require("../models/user.model");
const ApiError = require("../utils/apiError");
const asyncHandler = require("../utils/asyncHandler");

const verifyJWT = asyncHandler(async (req, _, next) => {
  const authHeader = req.header("Authorization") || "";
  const [scheme, token] = authHeader.split(" ");

  if (scheme !== "Bearer" || !token) {
    throw new ApiError(401, "Missing or invalid Authorization header");
  }

  let decodedToken;
  try {
    decodedToken = jwt.verify(token, process.env.JWT_SECRET);
  } catch (_) {
    throw new ApiError(401, "Invalid or expired access token");
  }

  const user = await User.findById(decodedToken?._id).select("-password");

  if (!user) {
    throw new ApiError(401, "Invalid access token");
  }

  req.user = user;
  next();
});

module.exports = verifyJWT;
