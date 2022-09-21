import java.util.ArrayList;
import java.util.List;

/**
 * Custom realisation of HashTable
 *
 * @param <K> key of HashTable
 * @param <V> value of HashTable
 */
public class HashTable<K,V> {
    /**
     * massive contains nodes of HashTable
     */
    private Node<K,V> [] table;
    /**
     * number of nodes in HashTable
     */
    private float hashTableSize=0;
    /**
     * constant for making a decision about increasing the array
     */
    private final float LOADFACTOR;

    public HashTable() {
        this.table = new Node[8];
        this.LOADFACTOR=0.7f;
    }
    /**
     * remove all nodes in HashTable
     */
    public void removeAll(){
        table= new Node[8];
    }
    /**
     * returns keys in descending order by String from method toString()
     */
    public List<K> getSortedKeys(){
        List<Node<K,V>> nodes = getAllNodes();
        List<K> keys=new ArrayList<>();
        nodes = quickSortByKeys(nodes);
        for (Node<K,V> node:nodes) {
            keys.add(node.getKey());
        }
        return keys;
    }
    /**
     * recursive quick sort algorithm for list of nodes, sort by value
     */
    private List<Node<K,V>> quickSortByKeys(List<Node<K,V>> nodes){
        if(nodes.size()<2){
            return nodes;
        }
        Node<K,V> pivot = nodes.get(0);
        nodes.remove(0);
        List<Node<K,V>> ans=new ArrayList<>();
        List<Node<K,V>> less=new ArrayList<>();
        List<Node<K,V>> greater= new ArrayList<>();
        for (Node<K,V> node:nodes) {
            if (node.getKey().toString().compareTo(pivot.getKey().toString())>0){
                less.add(node);
            }
            else {
                greater.add(node);
            }
        }
        ans=quickSortByKeys(greater);
        ans.add(pivot);
        ans.addAll(quickSortByKeys(less));
        return ans;
    }
    /**
     * remove node by key
     */
    public void remove(K key){
        Node<K,V> previousNode=null;
        Node<K,V> node=table[getTableIndexByHashCode(key.hashCode(),table.length)];
        while (node!=null){
            if(node.getKey().hashCode()==key.hashCode()){
                if (node.next!=null){
                    if(previousNode==null){
                        table[getTableIndexByHashCode(key.hashCode(),table.length)]=node.next;
                        return;
                    }
                    else {
                        table[getTableIndexByHashCode(key.hashCode(),table.length)]=null;
                        return;
                    }
                }
                else if(previousNode==null){
                    table[getTableIndexByHashCode(key.hashCode(),table.length)]=null;
                    return;
                }
                else {
                    previousNode.next=null;
                    return;
                }
            }
            previousNode=node;
            node=node.next;

        }
    }
    /**
     * put node with resize of table or not
     */
    public void put(K key, V value){
        if((hashTableSize/table.length)>=LOADFACTOR){
            Node<K,V> [] newTable = new Node[table.length*2];
            List<Node<K,V>> nodes =getAllNodes();
            for (Node<K,V> node:nodes) {
                add(node.getKey(), node.getValue(), newTable);
            }
            add(key, value, newTable);
            table = newTable;
        }
        else{
            add(key, value, table);
        }
    }
    /**
     * return all nodes
     */
    public List<Node<K,V>> getAllNodes(){
        List<Node<K,V>> nodes=new ArrayList<>();
        for(int i=0;i<table.length;i++){
            if(table[i]!=null){
                Node<K,V> node=table[i];
                nodes.add(node);
                while (node.next!=null){
                    node=node.next;
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }
    /**
     * put node by key
     */
    private void add(K key, V value, Node<K,V> [] table){
        int hashTableIndex;
        if(key != null){
            hashTableIndex= getTableIndexByHashCode(key.hashCode(),table.length);
            Node<K,V> node=table[hashTableIndex];
            if(node==null){
                table[hashTableIndex]=new Node<K,V>(hashTableIndex,key,value);
            }
            else {
                while (node.next!=null){
                    node=node.next;
                }
                node.next=new Node<K,V>(hashTableIndex,key,value);
            }
            hashTableSize++;
        }
    }
    /**
     * return value by key
     */
    public V getValue(K key){
        Node<K,V> node=table[getTableIndexByHashCode(key.hashCode(),table.length)];
        while (node!=null){
            if(node.getKey().hashCode()==key.hashCode()){
                return node.getValue();
            }
            node=node.next;
        }
        return null;
    }
    /**
     * makes array index by hashCode
     */
    private int getTableIndexByHashCode(int hashCode,int hashTableSize){
        int a=(hashTableSize-1) & hashCode;
        return (hashTableSize-1) & hashCode;
    }
    /**
     * class for storing key value pair, hash and link to link to another node in case of collision
     */
    private class Node<K,V>{
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next=null;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
