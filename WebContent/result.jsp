<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" import="web.src.models.*,java.util.*" %>
<%@ page import="web.src.models.Page" %>
<%@ page import="web.src.models.News" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
    List<News> arrlist = (List<News>) request.getAttribute("newslist");
    String queryback = (String) request.getAttribute("queryback");
    int totalnews = (Integer) request.getAttribute("totaln");
    double time = Double.parseDouble(request.getAttribute("time").toString());
    Page pageInfo = (Page) request.getAttribute("page");
    int perPageCount = (Integer) request.getAttribute("perPageCount");  // 每一页要显示几个结果
    int p = 1, i;
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>HITS-毕业设计</title>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <script type="text/javascript">
        window.onload = function () {
            document.getElementById("query").value = "<%=queryback%>";
        }

    </script>
</head>
<body>
<div class="nav">
    <div class="nav_left">
        <a href="index.jsp"><img alt="logo" src="images/logo.png"></a>
    </div>

    <div class="nav_right">
        <div class="nav_form">
            <form action="search" method="get">
                <input id="query" type="text" name="query" value=" ">
                <input type="submit" value="搜索"><br/>
                <div class="sortPick">
                    按相关度排序 <input type="radio" value="byreRelevancy" name="sortnews" checked="checked">
                    按时间排序 <input type="radio" value="byTime" name="sortnews">

                </div>
            </form>

        </div>
    </div>
</div>

<div class="newmain">
    <h4>

        共搜到<span class="newsnum"><%=totalnews%></span>条结果|用时<span
            class="newsnum"><%=time%></span>秒
    </h4>
    <%
        if (arrlist.size() > 0) {
            Iterator<News> iter = arrlist.iterator();
            News news;
            while (iter.hasNext()) {
                news = iter.next();
    %>

    <div class="item">
        <h4>
            <a href="<%=news.getURL()%> " target="_blank"><%=news.getTitle()%>
            </a>
        </h4>
        <p>
            <%=news.getArtical().length() > 200 ? news.getArtical().substring(0, 200) : news.getArtical()%>
            <br/>
            <a href=""><%=news.getURL()%>
            </a>
        </p>
    </div>
    <%
            }
        }
    %>
</div>
<div class="paging">
    <ul>
        <%
            // 如果不是第一页
            if (pageInfo.getPage() != 1) {
        %>
        <li>
            <a href="search?query=<%=queryback%>&&p=1">首页 </a>
        </li>
        <li>
            <a href="search?query=<%=queryback%>&&p=<%=pageInfo.getPage()-1==0?1:pageInfo.getPage()-1%>">上一页</a>
        </li>
        <%
            }
        %>
        <%
            // 总共有多少页
            int totalPages = totalnews / perPageCount + 1;

            // 超过 10 页, 只显示 10 页
            if (pageInfo.getPage() + 10 < totalPages) {
                // 则最多只显示 10 页
                totalPages = pageInfo.getPage() + 10;
            }

            for (i = pageInfo.getPage(); i <= totalPages; i++) {
        %>
        <li>
            <a href="search?query=<%=queryback%>&&p=<%=i%>">
                <%=i%>
            </a>
        </li>
        <%
            }
        %>
        <%
            // 如果当前页数 < 总页数
            if (pageInfo.getPage() < totalnews / perPageCount + 1) {
        %>
        <li><a href="search?query=<%=queryback%>&&p=<%=pageInfo.getPage()+1%>">下一页</a></li>
        <%
            }
        %>
    </ul>
    <hr>
</div>
<div class="footerinfo">
    <p>2017-西电-网信院-林丰-13030110024-毕业设计-designed by <a href="http://watch0.top">w@tch</a></p>
</div>
</body>
</html>