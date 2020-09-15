<#include "../layout/front/no_layout.ftl"/>

<@html title_="[简历]柯真-JAVA开发工程师">

<div>

    <#include "../layout/front/header.ftl"/>

    <article class="container">
        <section class="side" id="side">

            <!-- 个人肖像 -->
            <section class="me">
                <section class="portrait">
                    <div class="loading">
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                    <!-- 头像照片 -->
                    <img class="avatar" src="https://oscimg.oschina.net/oscnet/up-7da09418d15c03e31e3ff4f8c0036c7d.jpg!/both/200x200" >
                </section>

                <h4 class="info-job">JAVA开发工程师 / 深圳</h4>

            </section>

            <!-- 基本信息 -->
            <section class="profile info-unit">
                <h2>
                    <i class="fa fa-user" aria-hidden="true"></i>基本信息</h2>
                <hr/>
                <ul>
                    <li>
                        <label>个人信息</label>
                        <span>柯真 / 男 / 25岁</span>
                    </li>
                    <li>
                        <label>手机号码</label>
                        <span>18671310745</span>
                    </li>
                    <li>
                        <label>邮箱</label>
                        <span>305389431@qq.com</span>
                    </li>
                    <li>
                        <label>微信</label>
                        <span>LOVE305389431</span>
                    </li>
                    <li>
                        <label>期望薪资</label>
                        <span>16K</span>
                    </li>
                </ul>
            </section>

            <!-- 联系方式 -->
            <section class="contact info-unit">
                <h2>
                    <i class="fa fa-phone" aria-hidden="true"></i>个人主页</h2>
                <hr/>
                <ul>
                    <li>
                        <label>个人网站</label>
                        <a href="https://www.oscer.net" target="_blank">oscer</a>
                    </li>
                    <li>
                        <label>Github</label>
                        <a href="https://my.oschina.net/kezhen" target="_blank">开源中国</a>
                    </li>
                    <li>
                        <label>gitee</label>
                        <a href="https://gitee.com/kzr" target="_blank">码云</a>
                    </li>
                   <#-- <li>
                        <label>github</label>
                        <a href="https://github.com/forever7776" target="_blank">github</a>
                    </li>-->
                </ul>
            </section>

            <!-- 技能点 -->
            <section class="skill info-unit">
                <h2>
                    <i class="fa fa-code" aria-hidden="true"></i>技能点</h2>
                <hr/>
                <ul>
                    <li>
                        <label>java</label>
                        <progress value="90" max="100"></progress>
                    </li>
                    <li>
                        <label>linux</label>
                        <progress value="85" max="100"></progress>
                    </li>
                    <li>
                        <label>spring</label>
                        <progress value="85" max="100"></progress>
                    </li>
                    <li>
                        <label>springboot</label>
                        <progress value="85" max="100"></progress>
                    </li>
                    <li>
                        <label>mysql</label>
                        <progress value="85" max="100"></progress>
                    </li>
                    <li>
                        <label>mybatis</label>
                        <progress value="80" max="100"></progress>
                    </li>
                    <li>
                        <label>python</label>
                        <progress value="30" max="100"></progress>
                    </li>
                </ul>
            </section>

            <!-- 技术栈 -->
            <div class="stack info-unit">
                    <h2><i class="fa fa-code" aria-hidden="true"></i>其他</h2>
                    <hr/>
                    <ul>
                        <li>
                            <label>前端</label>
                            <span>BootStrap、Layui、Gulp、JQuery</span>
                        </li>
                        <li>
                            <label>后端</label>
                            <span>Java、Python、Spring、Springboot、MySQL、Mybatis、MybatisPlus、Elasticsearch</span>
                        </li>
                        <li>
                            <label>其他</label>
                            <span>Git、SVN、Markdown</span>
                        </li>
                    </ul>
                </div>
        </section>

        <section class="main">

            <!-- 工作经历 -->
            <section class="work info-unit">
                <h2>
                    <i class="fa fa-shopping-bag" aria-hidden="true"></i>工作经历</h2>
                <hr/>
                <ul>
                    <li>
                        <h3>
                            <span>深圳市奥思网络科技有限公司</span>
                            <span class="link">
                                <a href="https://www.oschina.net/" target="_blank">开源中国</a>
                            </span>
                            <time>2018.05-至今</time>
                        </h3>
                        <ul class="info-content">
                            <li>深度参与<mark><a href="https://www.oschina.net" target="_blank">开源中国社区</a></mark>项目改版后端开发工作，独立承担并完成
                                <mark><a href="https://my.oschina.net/kezhen" target="_blank">个人空间</a></mark>、
                                <mark><a href="https://www.oschina.net/blog" target="_blank">博客</a></mark>、
                                <mark><a href="https://www.oschina.net/question" target="_blank">问答</a></mark>、
                                <mark><a href="https://www.oschina.net/oschina-10th-anniversary/lottery" target="_blank">开源中国10周年抽奖</a></mark>、
                                <mark><a href="https://www.oschina.net/project/top_cn_2018" target="_blank">2018年开源软件评选</a></mark>、
                                <mark><a href="https://www.oschina.net/search?scope=all&q=j2cache" target="_blank">新版搜索</a></mark>
                                等模块的开发，修复与维护社区项目,解决bug,改善社区用户体验；
                                </li>
                            <li>开发<mark><a href="https://www.oschina.net/app" target="_blank">客户端</a></mark>需要的api接口，如果博客列表，问答列表等接口；</li>
                            <li>参与Itags智能推荐系统开发，其中参与数据同步（ETL）模块、服务接口（GateWay）模块、Elasticsearch（SearchApi）搜索模块；</li>
                            <li>项目采用技术：Servlet+
                                <mark><a href="https://www.oschina.net/p/dbutils" target="_blank">Dbutils</a></mark>+
                                <mark><a href="https://www.oschina.net/p/velocity" target="_blank">Velocity</a></mark>+
                                <mark><a href="https://www.oschina.net/p/j2cache" target="_blank">J2cache</a></mark>+
                                <mark><a href="https://www.oschina.net/p/elasticsearch" target="_blank">Elasticsearch</a></mark>+
                                <mark><a href="https://www.oschina.net/p/jcseg" target="_blank">Jcseg</a></mark></li>
                        </ul>
                    </li>
                    <li>
                        <h3>
                            <span>深圳启会科技发展有限公司</span>
                            <span class="link">
                                <a href="https://www.zhihuihuiwu.com/" target="_blank">智慧会务</a>
                            </span>
                            <time>2017.04-2018.04</time>
                        </h3>
                        <ul class="info-content">
                            <li>
                                负责<mark><a href="http://www.zhihuihuiwu.com" target="_blank">智慧会务平台</a></mark>
                                开发新功能：优惠码，报名凭证页广告，邮件系统(sendcloud)，
                                <mark><a href="http://www.zhihuihuiwu.com/faceScreen" target="_blank">签到投屏</a></mark>
                                、报名图表统计、服务商、系统图库、人脸识别(
                                <mark><a href="https://www.faceplusplus.com.cn" target="_blank">Face++</a></mark>)，
                                <mark><a href="http://www.minivision.cn/" target="_blank">小视科技</a></mark>、周边服务、邀请码、
                                名片识别(名片全能王)以及主项目整体改版和维护，修复遗留的bug，完善项目整体流程，塑造项目的健壮性，优化用户体验流程；</li>
                            <li>参与优化了短信，邮箱接口，开发了服务商统计，项目收费统计，周边服务统计；后台系统改版；编辑分布式子项目数据接口，完善子项目功能迭代；</li>
                            <li>项目采用技术：Springboot+Dubbo+RabbitMQ+Mybatis+Qiniu</li>
                        </ul>
                    </li>
                    <li>
                        <h3>
                            <span>深圳果菜贸易有限公司</span>
                            <time>2015.08-2017.04</time>
                        </h3>
                        <ul class="info-content">
                            <li>负责电商平台新功能开发（商品管理，商品分享，购物车模块，商品支付退款模块，微信会员充值，微信消息通知，财务统计等）；
                                新增商城模板，丰富商城样板；日常维护阿里云服务器正常运行。
                            <li>负责仓库管理，客户管理，出库入库，仓库查询，库存统计功能开发；</li>
                            <li>项目采用技术：Jfinal+微信SDK+easyUI</li>
                        </ul>
                    </li>
                </ul>

            </section>

            <!-- 项目经验 -->
            <section class="project info-unit">
                <h2>
                    <i class="fa fa-paper-plane-o" aria-hidden="true"></i>个人项目</h2>
                <hr/>
                <ul>
                    <li>
                        <h3>
                            <span>博客平台</span>
                            <span class="link">
                                <a href="http://oscer.net" target="_blank">oscer</a>
                            </span>
                            <time>2018.03-2018.06</time>
                        </h3>
                        <ul class="info-content">
                            <li>技术栈：Servlet+Dbutils+Velocity+J2cache+Elasticsearch+jieba</li>
                            <li>
                                <i class="fa fa-bars" aria-hidden="true"></i>
                                功能有：博客，备忘录，相册。。。
                                <br/>
                                <i class="fa fa-users" aria-hidden="true"></i>
                                [愿景]提供一个简单实用互帮互助的博客平台供大家使用
                                <br/>
                                <i class="fa fa-thumbs-o-up" aria-hidden="true"></i>
                                欢迎大家使用</li>
                        </ul>
                    </li>
                </ul>
            </section>

            <!-- 自我评价 -->
            <section class="work info-unit">
                <h2>
                    <i class="fa fa-pencil" aria-hidden="true"></i>自我评价/期望</h2>
                <hr/>
                <p>
                    <span class="mark-txt">“喜欢搞点有趣的东西”</span>不服输，有强迫症，性能一定要达到极致</p>
            </section>

            <!-- 教育经历 -->
            <section class="edu info-unit">
                <h2>
                    <i class="fa fa-graduation-cap" aria-hidden="true"></i>教育经历</h2>
                <hr/>
                <ul>
                    <li>
                        <h3>
                            <span>武汉软件工程职业学院 - 软件工程专业</span>
                            <time>2012.09-2015.07</time>
                        </h3>
                    </li>
                </ul>
            </section>

        </section>
    </article>



    <footer class="footer">
        <p>© 2020 柯真</p>
    </footer>

    <!-- 侧栏 -->
    <aside>
        <ul>
            <li>
                <a href="http://117.48.200.114:83/JAVA简历-柯真.docx" target="_blank">下载简历</a>
            </li>
            <li>
                <a href="https://my.oschina.net/kezhen" target="_blank">Blog</a>
            </li>
        </ul>
    </aside>


</div>

<script>
    function loading() {
        document.getElementsByClassName('avatar')[0].style.display = 'block';
        document.getElementsByClassName('loading')[0].style.display = 'none';
    }
</script>
</@html>