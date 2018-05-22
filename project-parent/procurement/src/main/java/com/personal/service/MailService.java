package com.personal.service;

public interface MailService {
    /**
     * 发送纯文本的简单邮件
     * @param to
     * @param subject
     * @param content
     */
    boolean sendSimpleMail(String to, String subject, String content);

    /**
     * 发送html格式的邮件
     * @param to
     * @param subject
     * @param content
     */
    boolean sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    boolean sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送嵌入静态资源（一般是图片）的邮件
     * @param to
     * @param subject
     * @param content 邮件内容，需要包括一个静态资源的id，比如：<img src=\"cid:rscId01\" >
     * @param rscPath 静态资源路径和文件名
     * @param rscId 静态资源id
     */
    boolean sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);
}
