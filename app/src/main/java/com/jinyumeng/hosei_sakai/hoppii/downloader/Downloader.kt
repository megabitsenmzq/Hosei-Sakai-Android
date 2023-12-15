package com.jinyumeng.hosei_sakai.hoppii.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}