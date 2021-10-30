package ir.urmia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PostIndex {
    private Integer[] postIndex;

    public PostIndex(Integer[] postIndex) {
        this.postIndex = postIndex;
    }

    public PostIndex and(PostIndex next) {
        Integer[] postIndex1 = this.postIndex;
        Integer[] postIndex2 = next.getPostIndex();
        ArrayList<Integer> res = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < postIndex1.length && j < postIndex2.length) {
            if (Objects.equals(postIndex1[i], postIndex2[j])) {
                res.add(postIndex1[i]);
                i++;
                j++;
            } else if (postIndex1[i] < postIndex2[j]) {
                i++;
            } else if (postIndex1[i] > postIndex2[j]) {
                j++;
            }
        }

        this.postIndex = new Integer[res.size()];

        System.arraycopy(res.toArray(), 0, this.postIndex, 0, res.size());
        return this;
    }

    public Integer[] getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(Integer[] postIndex) {
        this.postIndex = postIndex;
    }

    public PostIndex or(PostIndex next) {
        Integer[] postIndex1 = this.postIndex;
        Integer[] postIndex2 = next.getPostIndex();
        ArrayList<Integer> res = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < postIndex1.length && j < postIndex2.length) {
            if (Objects.equals(postIndex1[i], postIndex2[j])) {
                res.add(postIndex1[i]);
                i++;
                j++;
            } else if (postIndex1[i] < postIndex2[j]) {
                res.add(postIndex1[i]);
                i++;
            } else if (postIndex1[i] > postIndex2[j]) {
                res.add(postIndex2[j]);
                j++;
            }
        }

        this.postIndex = new Integer[res.size()];

        System.arraycopy(res.toArray(), 0, this.postIndex, 0, res.size());
        return this;
    }

    public PostIndex not(int maxId) {
        Integer[] postIndex1 = this.postIndex;
        ArrayList<Integer> res = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (i < postIndex1.length && j <= maxId) {
            if (postIndex1[i] < j) {
                i++;
            } else if (postIndex1[i] > j) {
                res.add(j);
                j++;
            } else {
                i++;
                j++;
            }
        }
        for (; j <= maxId; j++) {
            res.add(j);
        }

        this.postIndex = new Integer[res.size()];

        System.arraycopy(res.toArray(), 0, this.postIndex, 0, res.size());
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(postIndex);
    }
}
