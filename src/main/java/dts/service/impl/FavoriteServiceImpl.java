package dts.service.impl;

import dts.dao.FavoriteDao;
import dts.dao.impl.FavoriteDaoImpl;
import dts.domain.Favorite;
import dts.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao dao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite fav = dao.isFavorite(Integer.parseInt(rid), uid);
        return fav!=null;
    }

    @Override
    public void addFavorite(String rid, int uid) {
        dao.addFavorite(Integer.parseInt(rid), uid);
    }
}
