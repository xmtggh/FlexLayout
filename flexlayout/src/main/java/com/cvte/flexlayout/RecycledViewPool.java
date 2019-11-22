package com.cvte.flexlayout;


import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public final class RecycledViewPool {
    //是否开启debug模式
    private static boolean DEBUG = false;
    /**
     * 默认的 最大的回收缓存数目
     */
    private static final int DEFAULT_MAX_SCRAP = 5;


    static class ScrapData {
        final ArrayList<BaseItemView> mScrapHeap = new ArrayList<>();
        int mMaxScrap = DEFAULT_MAX_SCRAP;
        long mCreateRunningAverageNs = 0;
        long mBindRunningAverageNs = 0;
    }

    /**
     * 废弃view的容器<viewtype,list>的形式存储
     * viewType为整形
     */
    SparseArray<ScrapData> mScrap = new SparseArray<>();


    /**
     * 清除所有的缓存
     */
    public void clear() {
        for (int i = 0; i < mScrap.size(); i++) {
            ScrapData data = mScrap.valueAt(i);
            data.mScrapHeap.clear();
        }
    }

    /**
     * 根据viewType设置最大的缓存数目
     *
     * @param viewType view的类型
     * @param max      最大数目的
     */
    public void setMaxRecycledViews(int viewType, int max) {
        ScrapData scrapData = getScrapDataForType(viewType);
        scrapData.mMaxScrap = max;
        final ArrayList<BaseItemView> scrapHeap = scrapData.mScrapHeap;
        while (scrapHeap.size() > max) {
            scrapHeap.remove(scrapHeap.size() - 1);
        }
    }

    /**
     * 返回给定视图类型的RecycledViewPool所持有的当前视图数
     */
    public int getRecycledViewCount(int viewType) {
        return getScrapDataForType(viewType).mScrapHeap.size();
    }

    /**
     * 从池中获取指定类型的ViewHolder，如果没有指定类型的ViewHolder，则获取{@Codenull}.
     *
     * @param viewType BaseItemView type.
     * @return BaseItemView of the specified type acquired from the pool, or {@code null} if none
     * are present.
     */
    @Nullable
    public BaseItemView getRecycledView(int viewType) {
        final ScrapData scrapData = mScrap.get(viewType);
        if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
            final ArrayList<BaseItemView> scrapHeap = scrapData.mScrapHeap;
            return scrapHeap.remove(scrapHeap.size() - 1);
        }
        return null;
    }

    /**
     * 获取缓存池中所有缓存对象的数目.
     *
     * @return 获取当前缓存对象池中所有对象的数目.
     */
    int size() {
        int count = 0;
        for (int i = 0; i < mScrap.size(); i++) {
            ArrayList<BaseItemView> baseItemViews = mScrap.valueAt(i).mScrapHeap;
            if (baseItemViews != null) {
                count += baseItemViews.size();
            }
        }
        return count;
    }

    /**
     * 讲废弃的view放入缓存池，若当前缓存池已满，则抛弃此对象
     *
     * @param scrap BaseItemView to be added to the pool.
     */
    public void putRecycledView(BaseItemView scrap) {
        final int viewType = scrap.getItemViewType();
        final ArrayList<BaseItemView> scrapHeap = getScrapDataForType(viewType).mScrapHeap;
        if (mScrap.get(viewType).mMaxScrap <= scrapHeap.size()) {
            return;
        }
        if (DEBUG && scrapHeap.contains(scrap)) {
            throw new IllegalArgumentException("this scrap item already exists");
        }
//        scrap.resetInternal();
        scrapHeap.add(scrap);
    }

    long runningAverage(long oldAverage, long newValue) {
        if (oldAverage == 0) {
            return newValue;
        }
        return (oldAverage / 4 * 3) + (newValue / 4);
    }

    void factorInCreateTime(int viewType, long createTimeNs) {
        ScrapData scrapData = getScrapDataForType(viewType);
        scrapData.mCreateRunningAverageNs = runningAverage(
                scrapData.mCreateRunningAverageNs, createTimeNs);
    }

    void factorInBindTime(int viewType, long bindTimeNs) {
        ScrapData scrapData = getScrapDataForType(viewType);
        scrapData.mBindRunningAverageNs = runningAverage(
                scrapData.mBindRunningAverageNs, bindTimeNs);
    }

    boolean willCreateInTime(int viewType, long approxCurrentNs, long deadlineNs) {
        long expectedDurationNs = getScrapDataForType(viewType).mCreateRunningAverageNs;
        return expectedDurationNs == 0 || (approxCurrentNs + expectedDurationNs < deadlineNs);
    }

    boolean willBindInTime(int viewType, long approxCurrentNs, long deadlineNs) {
        long expectedDurationNs = getScrapDataForType(viewType).mBindRunningAverageNs;
        return expectedDurationNs == 0 || (approxCurrentNs + expectedDurationNs < deadlineNs);
    }

    /**
     * 根据类型获取废弃数据的对象
     *
     * @param viewType
     * @return
     */
    private ScrapData getScrapDataForType(int viewType) {
        ScrapData scrapData = mScrap.get(viewType);
        if (scrapData == null) {
            scrapData = new ScrapData();
            mScrap.put(viewType, scrapData);
        }
        return scrapData;
    }
}