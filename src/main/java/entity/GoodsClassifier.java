package entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by sdww on 15-10-30.
 */
public class GoodsClassifier {

    private DictionaryTree dictionaryTree;

    /**
     * 单例类获取实例方法
     * @param path 免税商品名单文件夹，该文件夹下所有文件均会被扫描，并将其中的单词都加入到字典树中
     * @return
     */
    public GoodsClassifier(String path) {

        //将所有免税商品名称加入字典树中
        File file = new File(path);
        File[] list = file.listFiles();
        dictionaryTree = new DictionaryTree();

        /*if(list == null) {
            System.out.println("build dictionaryTree failed");
        }*/

        for (File taxExempt : list) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(taxExempt));
                String lineString = null;
                while((lineString = reader.readLine()) != null) {
                    lineString =  lineString.replaceAll("\\s{2,}", " ");
                    dictionaryTree.add(lineString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
