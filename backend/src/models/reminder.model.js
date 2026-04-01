const mongoose = require("mongoose");

const reminderSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
    },
    title: {
      type: String,
      required: true,
      trim: true,
    },
    reminderTime: {
      type: String, // format "HH:mm"
      required: true,
    },
    enabled: {
      type: Boolean,
      default: true,
    },
    repeatType: {
      type: String,
      enum: ["DAILY", "WEEKDAYS", "ONCE"],
      default: "DAILY",
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("Reminder", reminderSchema);
