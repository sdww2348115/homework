package entity;

/**
 * Created by sdww on 15-10-30.
 */
public class DictionaryTree {
    //商品名称由a-zA-z加上空格符组成
    private static final int chSize = 'z' - 'A' + 1;

    private TreeNode root = new TreeNode();

    class TreeNode {
        private boolean isEnd = false;
        private Byte ch;
        private TreeNode[] next = new TreeNode[chSize];
        public TreeNode() {}
        public TreeNode(Byte ch) {
            this.ch = ch;
        }
    }

    /**
     * 将该单词添加到单词字典树中
     * @param str
     */
    public void add(String str) {
        if(str == null) return;
        byte[] bytes = str.getBytes();
        TreeNode temp = root;
        try {
            for(byte byt:bytes) {
                if(byt == ' ') {
                    if(temp.next[chSize - 1] == null) {
                        temp.next[chSize - 1] = new TreeNode(byt);
                    }
                    temp = temp.next[chSize - 1];
                }else {
                    if(temp.next[byt - 'A'] == null) {
                        temp.next[byt - 'A'] = new TreeNode(byt);
                    }
                    temp = temp.next[byt - 'A'];
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        //以该字符为结尾的单词属于免税商品
        temp.isEnd = true;
    }

    /**
     * 查看该单词是否属于该单词字典树
     * @param str
     * @return
     */
    public boolean isBelong(String str) {
        byte[] bytes = str.getBytes();
        TreeNode temp = root;
        try {
            for(byte byt:bytes) {
                if(byt == ' ') {
                    if(temp.next[chSize - 1] == null) {
                        return false;
                    }else {
                        temp = temp.next[chSize - 1];
                    }
                } else {
                    if(temp.next[byt - 'A'] == null) {
                        return false;
                    }else {
                        temp = temp.next[byt - 'A'];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //判断该字符是否属于某个免税商品名称结尾
        if(temp.isEnd == true) {
            return true;
        }else {
            return false;
        }
    }
}
