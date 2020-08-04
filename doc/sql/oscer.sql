/*
 Navicat MySQL Data Transfer

 Source Server         : 114.67.207.123
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : 114.67.207.123:3306
 Source Schema         : lifes77

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 04/08/2020 18:30:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL,
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `role` int(0) NULL DEFAULT NULL COMMENT '权限代码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user`(`user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for album_lists
-- ----------------------------
DROP TABLE IF EXISTS `album_lists`;
CREATE TABLE `album_lists`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `album_id` int(0) NOT NULL COMMENT '相册集ID',
  `album_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册url',
  `album_size` int(0) NOT NULL COMMENT '相册大小，kb',
  `album_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相册名',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_album_id`(`album_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '具体相册' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for albums
-- ----------------------------
DROP TABLE IF EXISTS `albums`;
CREATE TABLE `albums`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `album_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册标题',
  `album_count` int(0) NOT NULL DEFAULT 0 COMMENT '相册数量',
  `album_banner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册封面',
  `sort` tinyint(0) NOT NULL DEFAULT 0 COMMENT '排序（越大越靠前）',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '相册集' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bad_words
-- ----------------------------
DROP TABLE IF EXISTS `bad_words`;
CREATE TABLE `bad_words`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `text` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blog_infos
-- ----------------------------
DROP TABLE IF EXISTS `blog_infos`;
CREATE TABLE `blog_infos`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `blog_id` int(0) NOT NULL COMMENT '博客ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '博客内容',
  `type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '个人记录类型（0：博客，1：日常记录 2：其它）',
  `produce` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0：原帖 ，1：转帖',
  `produce_url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转帖地址',
  `comment` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否允许博客被评论（0：否 1：是）',
  `comment_show` tinyint(1) NOT NULL DEFAULT 1 COMMENT '评论是否显示（0：隐藏 1：显示）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_blog_id`(`blog_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客拓展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blog_types
-- ----------------------------
DROP TABLE IF EXISTS `blog_types`;
CREATE TABLE `blog_types`  (
  `id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '博客类型名称(如：情感，技术，工作，生活。。。。)',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '博客类型icon链接',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：否，1是）',
  `sort` tinyint(0) NOT NULL DEFAULT 1 COMMENT '排序',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父级url的id',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`, `sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blogs
-- ----------------------------
DROP TABLE IF EXISTS `blogs`;
CREATE TABLE `blogs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '博客ID',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID(默认为系统博客)',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '博客标题',
  `abstracts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '博客摘要',
  `banner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '博客封面',
  `openup` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否对平台开放(0:否 1：是)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（-1：用户隐藏， 0：系统隐藏， 1：显示）',
  `recommend_type` tinyint(1) NULL DEFAULT 1 COMMENT '推荐：数字越大越在前，默认1',
  `recommend_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '系统推荐时间',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '阅读数',
  `comment_count` int(0) NULL DEFAULT 0 COMMENT '评论数',
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞数',
  `collect_count` int(0) NULL DEFAULT 0 COMMENT '收藏数',
  `catalog` int(0) NOT NULL DEFAULT 1 COMMENT '博客分类id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_openup`(`openup`) USING BTREE,
  INDEX `idx_status_openup`(`openup`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collect_blogs
-- ----------------------------
DROP TABLE IF EXISTS `collect_blogs`;
CREATE TABLE `collect_blogs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `blog_id` int(0) NOT NULL COMMENT '第三方ID（博客ID,动弹ID，社区ID...）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '0：收藏 1：不收藏',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userid_blogid`(`user_id`, `blog_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collect_questions
-- ----------------------------
DROP TABLE IF EXISTS `collect_questions`;
CREATE TABLE `collect_questions`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `question` int(0) NOT NULL COMMENT '帖子ID',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '0：收藏 1：不收藏',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_question`(`user`, `question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_blogs
-- ----------------------------
DROP TABLE IF EXISTS `comment_blogs`;
CREATE TABLE `comment_blogs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `blog_id` int(0) NOT NULL COMMENT '博客ID',
  `user_id` int(0) NOT NULL COMMENT '评论者ID',
  `reply_user_id` int(0) NULL DEFAULT 0 COMMENT '回复评论者ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论的内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（0：系统冻结 1：正常显示）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_blog_id`(`blog_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_questions
-- ----------------------------
DROP TABLE IF EXISTS `comment_questions`;
CREATE TABLE `comment_questions`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `question` int(0) NOT NULL COMMENT '帖子ID',
  `user` int(0) NOT NULL COMMENT '评论者ID',
  `parent` int(0) NULL DEFAULT 0 COMMENT '回复的ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论的内容',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态（1：系统冻结 0：正常显示）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞数量',
  `reply_count` int(0) NULL DEFAULT 0 COMMENT '回复数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_question`(`question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_tweets
-- ----------------------------
DROP TABLE IF EXISTS `comment_tweets`;
CREATE TABLE `comment_tweets`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `tweet_id` int(0) NOT NULL COMMENT '动弹ID',
  `user_id` int(0) NOT NULL COMMENT '评论者ID',
  `reply_user_id` int(0) NULL DEFAULT NULL COMMENT '回复评论者ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论的内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（0：系统冻结 1：正常显示）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_tweet_id`(`tweet_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动弹评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dynamics
-- ----------------------------
DROP TABLE IF EXISTS `dynamics`;
CREATE TABLE `dynamics`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `question` int(0) NOT NULL COMMENT '帖子ID',
  `status` int(0) NOT NULL DEFAULT 0 COMMENT '显示状态，正常显示为0，如用户被封号，就隐藏',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `comment` int(0) NULL DEFAULT NULL COMMENT '评论ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_question`(`question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件原名',
  `size` int(0) NOT NULL DEFAULT 0 COMMENT '文件大小',
  `new_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件新名',
  `ext` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件后缀',
  `type` tinyint(0) NOT NULL COMMENT '所属类型',
  `catalog` tinyint(0) NOT NULL DEFAULT 1 COMMENT '文件类型',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `last_date` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for follows
-- ----------------------------
DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `follow_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被关注用户ID',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friendly_links
-- ----------------------------
DROP TABLE IF EXISTS `friendly_links`;
CREATE TABLE `friendly_links`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '友情链接---公司名称',
  `tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接网址',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接图片',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `sort` tinyint(0) NULL DEFAULT 1 COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（是否删除：0-否 1-是）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友情链接' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL,
  `friend` int(0) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_friend`(`user`, `friend`) USING BTREE,
  INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_friend`(`friend`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注/粉丝 表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for last_msgs
-- ----------------------------
DROP TABLE IF EXISTS `last_msgs`;
CREATE TABLE `last_msgs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL,
  `friend` int(0) NOT NULL,
  `sender` int(0) NOT NULL COMMENT '发送者',
  `receiver` int(0) NOT NULL COMMENT '接收者',
  `content` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '私信内容',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `last_update` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私信类型（系统私信：0，个人私信：1）',
  `msg_id` int(0) NOT NULL COMMENT '私信ID',
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '私信来源',
  `count` int(0) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx`(`user`, `friend`, `sender`, `receiver`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '最后一条私信表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for memos
-- ----------------------------
DROP TABLE IF EXISTS `memos`;
CREATE TABLE `memos`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '备忘录主键ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备忘录标题',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `memo_time` datetime(0) NOT NULL COMMENT '纪念日（如：出生日，结婚纪念日等）',
  `memo_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '类型（农历-1，阴历-0）',
  `remind_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提醒邮件',
  `remind_mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提醒手机',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `last_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户备忘录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msgs
-- ----------------------------
DROP TABLE IF EXISTS `msgs`;
CREATE TABLE `msgs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL,
  `friend` int(0) NOT NULL,
  `sender` int(0) NOT NULL COMMENT '发送者',
  `receiver` int(0) NOT NULL COMMENT '接收者',
  `content` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '私信内容',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `last_update` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私信类型（系统私信：0，个人私信：1）',
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '私信来源（网页端，小程序）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sender`(`sender`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '私信详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nodes
-- ----------------------------
DROP TABLE IF EXISTS `nodes`;
CREATE TABLE `nodes`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名称',
  `info` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  `sort` tinyint(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `url` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `parent` int(0) NULL DEFAULT 0 COMMENT '父节点',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for photos
-- ----------------------------
DROP TABLE IF EXISTS `photos`;
CREATE TABLE `photos`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user` int(0) UNSIGNED NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `year` tinyint(0) NULL DEFAULT NULL,
  `month` tinyint(0) NULL DEFAULT NULL,
  `day` tinyint(0) NULL DEFAULT NULL,
  `upload_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_year_month`(`year`, `month`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for praises
-- ----------------------------
DROP TABLE IF EXISTS `praises`;
CREATE TABLE `praises`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `obj_id` int(0) NOT NULL COMMENT '对象ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1：点赞 2：不点赞',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1：博客 2：动弹 3：评论',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_obj_id`(`obj_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动弹点赞详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '状态（0：显示 1：隐藏 2：待审核）',
  `top` tinyint(0) NULL DEFAULT 0 COMMENT '是否置顶（1：置顶）,自己置顶',
  `banner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视图封面',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '阅读量',
  `collect_count` int(0) NULL DEFAULT 0 COMMENT '收藏量',
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞量',
  `comment_count` int(0) NULL DEFAULT 0 COMMENT '评论量',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `forbid_comment` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁止评论（1：禁止）',
  `recomm` tinyint(1) NULL DEFAULT 0 COMMENT '是否推荐（1：推荐）',
  `original` tinyint(1) NULL DEFAULT 0 COMMENT '是否原创（1：不是，即转帖）',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转帖地址',
  `last_comment_user` int(0) NULL DEFAULT NULL COMMENT '最后的回帖人',
  `last_comment_time` datetime(0) NULL DEFAULT NULL COMMENT '最后的回帖时间',
  `reward_point` int(0) NULL DEFAULT 0 COMMENT '悬赏的积分',
  `reward_comment` int(0) NULL DEFAULT 0 COMMENT '被悬赏者，即最佳回帖人',
  `node` int(0) NOT NULL COMMENT '节点ID',
  `system_top` int(0) NOT NULL DEFAULT 0 COMMENT '系统置顶',
  `whole_node` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '完整节点，如 /1/2/3，最多三级节点',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_recomm`(`recomm`) USING BTREE,
  INDEX `idx_node`(`node`) USING BTREE,
  INDEX `idx_whole_node`(`whole_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reminds
-- ----------------------------
DROP TABLE IF EXISTS `reminds`;
CREATE TABLE `reminds`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `type` tinyint(0) NOT NULL COMMENT '类型（1:博客，2:动弹，3:提到别人，即@某人，4:回复某人）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `sender` int(0) NOT NULL COMMENT '主动提及的用户ID',
  `refer` int(0) NOT NULL COMMENT '提及到的用户ID',
  `status_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已读状态（0：未读，1：已读）',
  `obj_id` int(0) NOT NULL COMMENT '对象ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_refer`(`refer`) USING BTREE,
  INDEX `idx_status`(`status_read`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提醒表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for send_email_records
-- ----------------------------
DROP TABLE IF EXISTS `send_email_records`;
CREATE TABLE `send_email_records`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `send_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人的邮箱',
  `receive_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收件人的邮箱',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送类型(admin:后台系统 web:用户系统 user:用户)',
  `sender` int(0) NOT NULL DEFAULT 0 COMMENT '发送者ID（0表示系统）',
  `receiver` int(0) NOT NULL DEFAULT 0 COMMENT '接受者ID',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送的时间',
  `last_date` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送的内容',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题',
  `email_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件模板类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`sender`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发送邮箱记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for send_email_templates
-- ----------------------------
DROP TABLE IF EXISTS `send_email_templates`;
CREATE TABLE `send_email_templates`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件内容',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板类型',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0：正常 1：删除',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_details
-- ----------------------------
DROP TABLE IF EXISTS `sign_details`;
CREATE TABLE `sign_details`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `sign_day` int(0) NOT NULL COMMENT '签到时间（如：20190529）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_sign_day`(`user`, `sign_day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '签到详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for signs
-- ----------------------------
DROP TABLE IF EXISTS `signs`;
CREATE TABLE `signs`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `sign_year` int(0) NOT NULL COMMENT '签到的年数，比如今年是2019年，那这个值就是2019',
  `last_sign_day` datetime(0) NOT NULL COMMENT '最后一次签到时间',
  `total_count` int(0) NOT NULL DEFAULT 0 COMMENT '总共累计签到多少天',
  `series_count` int(0) NOT NULL DEFAULT 0 COMMENT '连续签到多少天，中断签到则为0',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次签到时间',
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user`(`user`) USING BTREE,
  INDEX `idx_sign_year`(`sign_year`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '签到表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_files
-- ----------------------------
DROP TABLE IF EXISTS `sys_files`;
CREATE TABLE `sys_files`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  `file_name` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `file_size` bigint(0) NOT NULL COMMENT '大小',
  `file_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '七牛KEY名',
  `file_suffix` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '后缀',
  `file_type` tinyint(0) NOT NULL DEFAULT 1 COMMENT '1:图片 2:视频',
  `file_path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `media_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信素材ID',
  `file_status` tinyint(0) NULL DEFAULT NULL COMMENT '文件上传成功状态（成功：0 失败 ：-1）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件插入时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件系统表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temperatures
-- ----------------------------
DROP TABLE IF EXISTS `temperatures`;
CREATE TABLE `temperatures`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL COMMENT '用户ID',
  `temperature` decimal(6, 2) NOT NULL COMMENT '体温：36.52',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `day` int(0) NULL DEFAULT NULL COMMENT '如：20190806',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '体温记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for topics
-- ----------------------------
DROP TABLE IF EXISTS `topics`;
CREATE TABLE `topics`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '话题名称',
  `tweet_id` int(0) NOT NULL COMMENT '动弹ID',
  `insert_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `sort` tinyint(0) NOT NULL DEFAULT 0 COMMENT '排序(越大越靠前)',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否显示（1：不显示）',
  `user_id` int(0) NOT NULL DEFAULT 0 COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '话题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tweets
-- ----------------------------
DROP TABLE IF EXISTS `tweets`;
CREATE TABLE `tweets`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容（如：文字，图片，表情等）',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（-1：用户隐藏， 0：系统隐藏， 1：显示）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `view_count` int(0) NULL DEFAULT 0 COMMENT '阅读数',
  `comment_count` int(0) NULL DEFAULT 0 COMMENT '评论数',
  `praise_count` int(0) NULL DEFAULT 0 COMMENT '点赞数',
  `collect_count` int(0) NULL DEFAULT 0 COMMENT '收藏数',
  `photos` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `thumbs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '缩略图',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动弹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_binds
-- ----------------------------
DROP TABLE IF EXISTS `user_binds`;
CREATE TABLE `user_binds`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user` int(0) NOT NULL,
  `provider` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称 ',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `headimg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint(0) NOT NULL DEFAULT 1,
  `from` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册来源',
  `union_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联合ID(微信小程序的openid)',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_openid_user`(`user`, `provider`) USING BTREE,
  INDEX `idx_openid_provider`(`provider`) USING BTREE,
  INDEX `idx_openid_account`(`name`) USING BTREE,
  INDEX `idx_openid_union_id`(`union_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方账号绑定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名,如jack)',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码(明文)',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密）',
  `headimg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `job` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `wx` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信',
  `wx_qrcode` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信二维码加好友，图片地址',
  `qq` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'qq',
  `sex` tinyint(1) NULL DEFAULT 0 COMMENT '性别（0：保密 1：男 2：女）',
  `self_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自我简介',
  `login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `logout_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登出时间(手动登出,系统登出即session超时时间)',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '冻结：-1 正常:0 封号：1  手动注销：2  系统销毁：3',
  `ident` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性域名',
  `login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `online` tinyint(1) NULL DEFAULT 0 COMMENT '登录状态（0:下线 1:上线）',
  `logo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `view_count` int(0) NULL DEFAULT 0 COMMENT '阅读数',
  `from` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册来源',
  `score` int(0) NULL DEFAULT 100 COMMENT '积分',
  `score_today` int(0) NULL DEFAULT 0 COMMENT '今天获得的积分',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for visits
-- ----------------------------
DROP TABLE IF EXISTS `visits`;
CREATE TABLE `visits`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `obj_id` int(0) NOT NULL COMMENT '对象ID',
  `obj_type` tinyint(0) NULL DEFAULT NULL COMMENT '对象类型（1:user,2:blog,3:tweet）',
  `insert_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_objid_objtype`(`user_id`, `obj_id`, `obj_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for UPDATE_USER_SCORE_TODAY
-- ----------------------------
DROP PROCEDURE IF EXISTS `UPDATE_USER_SCORE_TODAY`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UPDATE_USER_SCORE_TODAY`()
BEGIN
  update users set score_today = 0;
END
;;
delimiter ;

-- ----------------------------
-- Event structure for UPDATE_SCORE_TODAY
-- ----------------------------
DROP EVENT IF EXISTS `UPDATE_SCORE_TODAY`;
delimiter ;;
CREATE DEFINER = `root`@`localhost` EVENT `UPDATE_SCORE_TODAY`
ON SCHEDULE
EVERY '1' DAY STARTS '2019-05-29 16:40:01'
DO CALL UPDATE_USER_SCORE_TODAY()
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
