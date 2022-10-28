package com.ei.zezoo;

public interface FragmentCommunicator{
    public void setConnectButton(boolean isConnected);
    public void startConnectAnimation();
    public void stopConnectAnimation();
    public void exitVideoLesson();
    public boolean isConnectAnimationStarted();
    public void setButtonDownloadV(LessonListChildItem lesson ,int v);
    public void setButtonStopV(LessonListChildItem lesson ,int v);
    public void setButtonPauseV(LessonListChildItem lesson ,int v);
    public void setButtonWatchV(LessonListChildItem lesson ,int v);
    public void setTextDownload(LessonListChildItem lesson ,String text);
    public void setProgress(LessonListChildItem lesson ,int p);
    public void setIsResume(LessonListChildItem lesson ,boolean is);
    public void setIsDownloading(LessonListChildItem lesson ,boolean is);
}
