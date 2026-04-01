const mongoose = require("mongoose");

const focusSessionSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
    },
    sessionTitle: {
      type: String,
      default: "Focus Session",
    },
    mode: {
      type: String,
      enum: ["Pomodoro", "Deep Work", "Custom"],
      default: "Deep Work",
    },
    duration: {
      type: Number, // in minutes
      required: true,
    },
    completed: {
      type: Boolean,
      default: false,
    },
    interrupted: {
      type: Boolean,
      default: false,
    },
    blockedAppsCount: {
      type: Number,
      default: 0,
    },
    distractionAttempts: {
      type: Number,
      default: 0,
    },
    startedAt: {
      type: Date,
      default: Date.now,
    },
    endedAt: {
      type: Date,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("FocusSession", focusSessionSchema);
