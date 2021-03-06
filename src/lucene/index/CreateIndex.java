package lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import web.src.models.News;
import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class CreateIndex {
    // TODO: 这里更改数据源
    static String source_data_path = "/Users/L1n/Desktop/Notes/毕设/毕设实现/工程文件/xml2json_result/";

    public static void main(String[] args) throws IOException {
        // 第一步：创建分词器
        Analyzer paodingAnalyzer = new PaodingAnalyzer();   // paoding 分词
        // Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);  // 单字分词

        // 第二步：创建indexWriter配置信息
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, paodingAnalyzer);
        // 第三步：设置索引的打开方式
        indexWriterConfig.setOpenMode(OpenMode.CREATE);
        // 第四步：设置索引第路径
        Directory directory = null;
        // 第五步:创建indexWriter,用于索引第增删改.
        IndexWriter indexWriter = null;

        // 创建索引要保存的路径
        try {
            File indexpath = new File("./WebContent/new_index");
            if (indexpath.exists() != true) {
                indexpath.mkdirs();
            }
            directory = FSDirectory.open(indexpath);
            if (indexWriter.isLocked(directory)) {
                indexWriter.unlock(directory);
            }
            indexWriter = new IndexWriter(directory, indexWriterConfig);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 循环创建索引
        ArrayList<String> fileNameList = getFileName();
        Iterator<String> iter = fileNameList.iterator();

        while (iter.hasNext()) {
            // System.out.println(iter.next());
            News news = getNews(source_data_path + iter.next());
            Document doc = new Document();
            if (news != null) {
                System.out.println(news.getTitle());

                doc.add(new TextField("news_id", news.getId(), Store.YES));
                doc.add(new TextField("news_title", news.getTitle(), Store.YES));
                doc.add(new TextField("news_article", news.getArticle(), Store.YES));
                doc.add(new TextField("news_url", news.getURL(), Store.YES));
            }
            try {
                indexWriter.addDocument(doc);
                indexWriter.commit();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            indexWriter.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("index create success!");

    }

    // 获取 news 目录下所有 json 文件的文件名,返回文件名数组
    public static ArrayList<String> getFileName() {
        ArrayList<String> arrlist = new ArrayList<>();
        File dataPth = new File(source_data_path);
        if (dataPth.exists()) {
            File[] allFiles = dataPth.listFiles();
            for (int i = 0; i < allFiles.length; i++) {
                arrlist.add(allFiles[i].getName().toString());
            }
        }

        System.out.println(arrlist.size());
        return arrlist;
    }

    // 把 json 文件解析为 News 对象,返回值为 News 对象
    public static News getNews(String path) throws IOException {
        News news = new News();
        try {
            JsonParser jParser = new JsonParser();
            JsonObject jObject = (JsonObject) jParser.parse(new FileReader(path));

            String url = jObject.get("url").getAsString();
            String content = jObject.get("content").getAsString();
            String doc_number = jObject.get("doc_number").getAsString();
            String content_title = jObject.get("content_title").getAsString();

            news = new News(url, content, doc_number, content_title);
            return news;
        } catch (IOException e) {
            System.out.println("解析 JSON 出错");
            throw e;
        }


    }
}
