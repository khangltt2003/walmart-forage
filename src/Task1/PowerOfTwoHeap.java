package Task1;

import java.util.ArrayList;
import java.util.Collections;

public class PowerOfTwoHeap  {
    private ArrayList<Integer> heap;
    private int numChild;

    public PowerOfTwoHeap(int x){
        this.numChild = (int) Math.pow(2, x);
        this.heap = new ArrayList<>();
    }

    public void insert(int x){
        heap.add(x);
        heapifyUp(heap.size() -1);
    }

    public int pop(){
        if(heap.size() == 0) return -1;
        int max = heap.get(0);

        int lastElement = heap.remove(heap.size() - 1);

        if(!heap.isEmpty()){
            //move last element to the top
            heap.set(0, lastElement);
            heapifyDown(0);
        }

        return max;
    }

    private void heapifyUp(int start){
        int parentIndex = indexOfParent(start);
        while(start > 0 && heap.get(start) > heap.get(parentIndex)){
            Collections.swap(heap, start, parentIndex);
            start = parentIndex;
            parentIndex = indexOfParent(start);
        }
    }
    private void heapifyDown(int start){
        while(true){
            int largestIndex = start;
            for(int i = 1; i <= numChild; i++){
                int indexOfChild = indexOfChild(start, i);
                if(indexOfChild < heap.size() && heap.get(largestIndex) < heap.get(indexOfChild) ){
                    largestIndex = indexOfChild;
                }
            }
            // stop when node is in the correct position
            if(largestIndex == start) break;
            Collections.swap(heap, largestIndex, start);
            start = largestIndex;
        }
    }

    private int indexOfParent(int indexOfChild){
        return (indexOfChild - 1)/ numChild;
    }

    private int indexOfChild(int indexOfParent, int childPos){
        return (indexOfParent * numChild) + childPos;
    }

    public static void main(String [] args){
        PowerOfTwoHeap heap = new PowerOfTwoHeap(2);
        heap.insert(1);
        heap.insert(9);
        heap.insert(2);
        heap.insert(8);
        heap.insert(5);
        heap.insert(100);
        heap.insert(200);
        heap.insert(0);

        System.out.println("Popped element: " + heap.pop());
        System.out.println("Popped element: " + heap.pop());
    }


}
