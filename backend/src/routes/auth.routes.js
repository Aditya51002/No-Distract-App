const { Router } = require("express");
const { register, login, getMe, updateProfile } = require("../controllers/auth.controller");
const verifyJWT = require("../middlewares/auth.middleware");

const router = Router();

router.route("/register").post(register);
router.route("/login").post(login);

// Protected routes
router.route("/me").get(verifyJWT, getMe);
router.route("/profile").put(verifyJWT, updateProfile);

module.exports = router;
