const mongoose = require("mongoose");

const distractionAttemptSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
    },
    sessionId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "FocusSession",
    },
    appName: {
      type: String,
      required: true,
    },
    packageName: {
      type: String,
      required: true,
    },
    attemptedAt: {
      type: Date,
      default: Date.now,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("DistractionAttempt", distractionAttemptSchema);
