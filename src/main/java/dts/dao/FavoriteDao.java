package dts.dao;

import dts.domain.Favorite;

public interface FavoriteDao {
    public Favorite isFavorite(int rid, int uid);
    public int favoriteCount(int rid);

    public void addFavorite(int parseInt, int uid);
}
