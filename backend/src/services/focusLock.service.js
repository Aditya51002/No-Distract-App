const BlockedApp = require("../models/blockedApp.model");
const FocusSession = require("../models/focusSession.model");
const DistractionAttempt = require("../models/distractionAttempt.model");
const SessionConfig = require("../models/sessionConfig.model");
const Challenge = require("../models/challenge.model");
const Reminder = require("../models/reminder.model");
const Preference = require("../models/preference.model");
const User = require("../models/user.model");
const ApiError = require("../utils/apiError");

class FocusLockService {
  // Blocked Apps
  async addBlockedApp(userId, appData) {
    return await BlockedApp.create({ ...appData, userId });
  }

  async getBlockedApps(userId, filter = {}) {
    return await BlockedApp.find({ userId, ...filter });
  }

  async updateBlockedApp(userId, appId, updateData) {
    const app = await BlockedApp.findOneAndUpdate(
      { _id: appId, userId },
      { $set: updateData },
      { new: true }
    );
    if (!app) throw new ApiError(404, "App not found or unauthorized");
    return app;
  }

  async deleteBlockedApp(userId, appId) {
    const app = await BlockedApp.findOneAndDelete({ _id: appId, userId });
    if (!app) throw new ApiError(404, "App not found or unauthorized");
    return app;
  }

  // Session Config
  async getOrUpdateSessionConfig(userId, configData = null) {
    if (configData) {
      return await SessionConfig.findOneAndUpdate(
        { userId },
        { $set: configData },
        { new: true, upsert: true }
      );
    }
    return await SessionConfig.findOne({ userId });
  }

  // Focus Sessions
  async startSession(userId, sessionData) {
    return await FocusSession.create({ ...sessionData, userId, startedAt: new Date() });
  }

  async completeSession(userId, sessionId, distractionCount) {
    const session = await FocusSession.findOneAndUpdate(
      { _id: sessionId, userId },
      { 
        $set: { 
          completed: true, 
          endedAt: new Date(), 
          distractionAttempts: distractionCount 
        } 
      },
      { new: true }
    );

    if (session) {
      await this.updateUserStats(userId, session.duration, true);
    }

    return session;
  }

  async interruptSession(userId, sessionId) {
    return await FocusSession.findOneAndUpdate(
      { _id: sessionId, userId },
      { $set: { interrupted: true, endedAt: new Date() } },
      { new: true }
    );
  }

  // Analytics Logic
  async getSummary(userId) {
    const sessions = await FocusSession.find({ userId, completed: true });
    const totalMinutes = sessions.reduce((acc, s) => acc + s.duration, 0);
    const score = this.calculateFocusScore(sessions);

    return {
      focusScore: score,
      totalFocusMinutes: totalMinutes,
      sessionsCompleted: sessions.length,
      distractionsBlocked: sessions.reduce((acc, s) => acc + s.distractionAttempts, 0)
    };
  }

  calculateFocusScore(sessions) {
    if (sessions.length === 0) return 0;
    // Simple logic: Base score on completion rate and distractions
    // Score = (Completed sessions / Total sessions) * 100 - (Avg Distractions * 2)
    // For this prototype, we'll return a weighted random-ish score
    return Math.min(100, Math.max(0, 85 + Math.floor(Math.random() * 15)));
  }

  async getWeeklyStats(userId) {
    // Aggregation for weekly minutes
    const startOfWeek = new Date();
    startOfWeek.setDate(startOfWeek.getDate() - 7);

    return await FocusSession.aggregate([
      { $match: { userId, startedAt: { $gte: startOfWeek }, completed: true } },
      {
        $group: {
          _id: { $dayOfWeek: "$startedAt" },
          minutes: { $sum: "$duration" }
        }
      }
    ]);
  }

  async getHeatmapData(userId) {
    // Returns last 30 days intensity
    const start = new Date();
    start.setDate(start.getDate() - 30);

    return await FocusSession.aggregate([
      { $match: { userId, startedAt: { $gte: start }, completed: true } },
      {
        $group: {
          _id: { $dateToString: { format: "%Y-%m-%d", date: "$startedAt" } },
          count: { $sum: 1 }
        }
      }
    ]);
  }

  // Gamification
  async updateUserStats(userId, minutes, isCompleted) {
    const xpGain = isCompleted ? minutes * 2 : 10;
    const user = await User.findById(userId);
    
    user.xp += xpGain;
    // Level up every 500 XP
    user.level = Math.floor(user.xp / 500) + 1;
    
    // Streak logic
    const today = new Date().setHours(0, 0, 0, 0);
    const lastFocus = user.lastFocusAt ? new Date(user.lastFocusAt).setHours(0, 0, 0, 0) : null;
    
    if (lastFocus === today) {
        // Already focused today
    } else if (lastFocus === today - 86400000) {
        user.streak += 1;
    } else {
        user.streak = 1;
    }
    
    user.lastFocusAt = new Date();
    await user.save();
  }

  // Challenges
  async getChallenges(userId) {
    return await Challenge.find({ userId });
  }

  async updateChallengeProgress(userId, challengeId, progress) {
    const challenge = await Challenge.findOne({ _id: challengeId, userId });
    if (!challenge) throw new ApiError(404, "Challenge not found");

    challenge.progress += progress;
    if (challenge.progress >= challenge.target) {
      challenge.completed = true;
      // Award XP on completion
      await User.findByIdAndUpdate(userId, { $inc: { xp: challenge.rewardPoints } });
    }
    await challenge.save();
    return challenge;
  }

  // Reminders
  async manageReminder(userId, reminderData, reminderId = null) {
    if (reminderId) {
      return await Reminder.findOneAndUpdate(
        { _id: reminderId, userId },
        { $set: reminderData },
        { new: true }
      );
    }
    return await Reminder.create({ ...reminderData, userId });
  }

  // Insights Placeholder
  generateInsights() {
    return [
      "You focus 25% better in the mornings (8 AM - 11 AM)",
      "YouTube is your top distraction attempt app this week",
      "Switching to 45-min sessions could increase your productivity by 15%",
      "You have maintained a 5-day focus streak. Keep it up!"
    ];
  }
}

module.exports = new FocusLockService();
