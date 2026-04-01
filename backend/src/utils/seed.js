require("dotenv").config();
const mongoose = require("mongoose");
const User = require("../models/user.model");
const BlockedApp = require("../models/blockedApp.model");
const Challenge = require("../models/challenge.model");
const { CHALLENGE_TYPES } = require("../constants");

const seedData = async () => {
  try {
    await mongoose.connect(process.env.MONGODB_URI);
    console.log("Connected to MongoDB for seeding...");

    // Clear existing data (optional)
    // await User.deleteMany({});
    // await BlockedApp.deleteMany({});
    // await Challenge.deleteMany({});

    // Create a dummy user if not exists
    let user = await User.findOne({ email: "aditya@example.com" });
    if (!user) {
      user = await User.create({
        name: "Aditya",
        email: "aditya@example.com",
        password: "password123", // Will be hashed by pre-save hook
      });
      console.log("Dummy user created");
    }

    // Seed Blocked Apps
    const apps = [
      { appName: "Instagram", packageName: "com.instagram.android", category: "Social", userId: user._id },
      { appName: "YouTube", packageName: "com.google.android.youtube", category: "Entertainment", userId: user._id },
      { appName: "Facebook", packageName: "com.facebook.katana", category: "Social", userId: user._id },
      { appName: "TikTok", packageName: "com.zhiliaoapp.musically", category: "Entertainment", userId: user._id },
    ];

    for (const app of apps) {
      await BlockedApp.findOneAndUpdate(
        { packageName: app.packageName, userId: user._id },
        app,
        { upsert: true }
      );
    }
    console.log("Blocked apps seeded");

    // Seed Challenges
    const challenges = [
      { title: "Morning Focus", target: 3, type: CHALLENGE_TYPES.DAILY, rewardPoints: 100, userId: user._id },
      { title: "No Distractions", target: 5, type: CHALLENGE_TYPES.WEEKLY, rewardPoints: 500, userId: user._id },
      { title: "Streak Master", target: 7, type: CHALLENGE_TYPES.WEEKLY, rewardPoints: 1000, userId: user._id },
    ];

    for (const challenge of challenges) {
      await Challenge.findOneAndUpdate(
        { title: challenge.title, userId: user._id },
        challenge,
        { upsert: true }
      );
    }
    console.log("Challenges seeded");

    process.exit(0);
  } catch (error) {
    console.error("Error seeding data:", error);
    process.exit(1);
  }
};

seedData();
