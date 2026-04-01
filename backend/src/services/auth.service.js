const User = require("../models/user.model");
const ApiError = require("../utils/apiError");

class AuthService {
  async registerUser(userData) {
    const { email } = userData;
    const existedUser = await User.findOne({ email });

    if (existedUser) {
      throw new ApiError(409, "User with email already exists");
    }

    const user = await User.create(userData);
    const createdUser = await User.findById(user._id).select("-password");

    if (!createdUser) {
      throw new ApiError(500, "Something went wrong while registering the user");
    }

    return createdUser;
  }

  async loginUser(email, password) {
    const user = await User.findOne({ email });

    if (!user) {
      throw new ApiError(404, "User does not exist");
    }

    const isPasswordValid = await user.isPasswordCorrect(password);

    if (!isPasswordValid) {
      throw new ApiError(401, "Invalid user credentials");
    }

    const accessToken = user.generateAccessToken();
    const loggedInUser = await User.findById(user._id).select("-password");

    return { user: loggedInUser, accessToken };
  }
}

module.exports = new AuthService();
