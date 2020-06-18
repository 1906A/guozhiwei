package com.leyou.search;


import com.leyou.common.PageResult;
import com.leyou.search.Item.Goods;
import com.leyou.search.client.SpuClient;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import vo.SpuVo;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LySearchApplicationTests {

     @Autowired
     SpuClient spuClient;

     @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

     @Autowired
     GoodsRepository goodsRepository;

     @Autowired
    GoodsService goodsService;

    @Test
    public void contextLoads() {
         //创建索引库
        elasticsearchTemplate.createIndex(Goods.class);
        //添加映射
        elasticsearchTemplate.putMapping(Goods.class);

        PageResult<SpuVo> spuByPage = spuClient.findSpuByPage("",1,200,2);
        spuByPage.getItems().forEach(spuVo -> {
            System.out.println(spuVo.getId());
            try {
                Goods goods = goodsService.convent(spuVo);
                goodsRepository.save(goods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
