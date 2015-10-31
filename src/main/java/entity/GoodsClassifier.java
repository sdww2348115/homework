package entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by sdww on 15-10-30.
 */
public class GoodsClassifier {
    //防止该类被new出来
    //private GoodsClassifier() {};
    //持有该单例对象
    //private static GoodsClassifier goodsClassifier = null;
    //免税商品名称的字典树
    private DictionaryTree dictionaryTree;

    /**
     * 单例类获取实例方法
     * @param path 免税商品名单文件夹，该文件夹下所有文件均会被扫描，并将其中的单词都加入到字典树中
     * @return
     */
    public static GoodsClassifier getInstance(String path) {
        GoodsClassifier goodsClassifier = new GoodsClassifier();
        goodsClassifier.dictionaryTree = new DictionaryTree();

        //将所有免税商品名称加入字典树中
        File file = new File(path);
        File[] list = file.listFiles();
        if(list == null) {
            System.out.println("build dictionarytree failed");
            return goodsClassifier;
        }
        for (File nonTaxPath : list) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(nonTaxPath));
                String lineString = null;
                while((lineString = reader.readLine()) != null) {
                    goodsClassifier.dictionaryTree.add(lineString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return goodsClassifier;
    }

    /**
     * 查看该商品是否为免税商品
     * @param name 商品名称
     * @return
     */
    public boolean isExempt(String name) {
        return dictionaryTree.isBelong(name);
    }
}
