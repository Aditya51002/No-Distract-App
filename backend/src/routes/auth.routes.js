const { Router } = require("express");
const { register, login, getMe, updateProfile } = require("../controllers/auth.controller");
const verifyJWT = require("../middlewares/auth.middleware");
const passport = require("passport");

const router = Router();

router.route("/register").post(register);
router.route("/login").post(login);

// OAuth Google
router.get("/google", passport.authenticate("google", { scope: ["profile", "email"] }));
router.get("/google/callback", passport.authenticate("google", { failureRedirect: "/login" }), (req, res) => {
  // Successful authentication, redirect or respond
  res.redirect("/");
});

// OAuth GitHub
router.get("/github", passport.authenticate("github", { scope: ["user:email"] }));
router.get("/github/callback", passport.authenticate("github", { failureRedirect: "/login" }), (req, res) => {
  // Successful authentication, redirect or respond
  res.redirect("/");
});

// Protected routes
router.route("/me").get(verifyJWT, getMe);
router.route("/profile").put(verifyJWT, updateProfile);

module.exports = router;
