package com.java456.lucene;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import com.java456.entity.blog.Blog;
import com.java456.properties.Proper;
import com.java456.util.DateUtil;
import com.java456.util.StringUtil;


/**
 * #博客索引类
 * @author Administrator
 */
public class BlogIndex {
	
	private Directory dir;
	
	public static String path ="";
	
	
	/**
	 *  # 获取IndexWriter实例    返回写索引实例 
	 *  
	 * #windows使用  C://lucene//java456_blog
	 * # linux  使用这个  /home/lucene/java456_blog
	 * @throws Exception
	 */
	private IndexWriter getWriter()throws Exception{
		dir=FSDirectory.open(Paths.get(path));//索引 写到什么地方，指定目录  ， Directory就是目录
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();// 中文分词器
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);//来设置其相关配置
		IndexWriter writer=new IndexWriter(dir, iwc);//我们最终是实例化这个东西。 IndexWriter 写索引实例 
		return writer;
	}
	
	
	/**
	 * #添加博客索引
	 * @param blog
	 * @throws Exception
	 */
	public void addIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();
		doc.add(new StringField("id",String.valueOf(blog.getId()),Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	}
	
	/**
	 * #更新博客索引
	 */
	public void updateIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();
		doc.add(new StringField("id",String.valueOf(blog.getId()),Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.updateDocument(new Term("id",String.valueOf(blog.getId())), doc);
		writer.close();
	}
	
	
	/**
	 * 删除指定博客的索引
	 * @param blogId,String RootPath
	 * @throws Exception
	 */
	public void deleteIndex(String blogId)throws Exception{
		IndexWriter writer=getWriter();
		writer.deleteDocuments(new Term("id",blogId));
		writer.forceMergeDeletes(); // 强制删除
		writer.commit();
		writer.close();
	}
	
	/**
	 *# 查询博客信息
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<Blog> searchBlog(String q)throws Exception{
		dir=FSDirectory.open(Paths.get(path));
		IndexReader reader=DirectoryReader.open(dir);
		IndexSearcher is=new IndexSearcher(reader);
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		QueryParser parser=new QueryParser("title", analyzer);
		Query query=parser.parse(q);
		
		QueryParser parser2=new QueryParser("content", analyzer);
		Query query2=parser2.parse(q);
		
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);//or
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		TopDocs hits=is.search(booleanQuery.build(), 100);
		
		//把得分高的片段摘要出来
		QueryScorer scorer=new QueryScorer(booleanQuery.build());
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		//把得分高的片段摘要出来
		
		//格式化前缀和后缀
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		//格式化前缀和后缀
		
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		
		List<Blog> blogList=new LinkedList<Blog>();
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			Blog blog=new Blog();
			blog.setId(Integer.parseInt(doc.get("id")));
			String title=doc.get("title");
			if(title!=null){
				TokenStream tokenStream=analyzer.tokenStream("title", new StringReader(title));
				String hTitle=highlighter.getBestFragment(tokenStream, title);
				
				if(StringUtil.isEmpty(hTitle)){
					blog.setTitle(title);
				}else{
					blog.setTitle(hTitle);
				}
			}
			//过虑掉html中的<标签>
			
			String content=doc.get("content");//这个content取得是  notag的content  索引中的
			//把<>转义成   &lt; <    &gt; >
			content = content.replace("<", "&lt;");
			content =  content.replace(">", "&gt;");
			//String content= doc.get("content");
			if(content!=null){
				TokenStream tokenStream=analyzer.tokenStream("content", new StringReader(content));
				String hContent=highlighter.getBestFragment(tokenStream, content);
				
				if(StringUtil.isEmpty(hContent)){
					if(content.length()<=200){
						blog.setContent(content);						
					}else{
						blog.setContent(content.substring(0, 200));	
					}
				}else{
					blog.setContent(hContent);
				}
			}
			blogList.add(blog);
		}
		return blogList;
	}
	
}
