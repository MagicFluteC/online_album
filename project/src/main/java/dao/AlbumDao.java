package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBUtil;
import po.Album;
import po.Photo;

public class AlbumDao {

    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ResultSet resultSet_temp = null;
    String sql = null;
    Connection connection = null;


    /**
     * c  关闭各种资源(使用resultset)
     */
    public void closeParaResources() {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * c 关闭资源(未使用resultset)
     */
    public void closeResources() {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * c 通过用户名查询
     */
    public Album findByAlbumName(String album_name) {

        sql = "select name,userid from album where name = ?";
        Album album = new Album();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, album_name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                album.setName(resultSet.getString("name"));
                album.setUserid(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeParaResources();
        return album;
    }


    /**
     * c注册服务
     */
    public void addAlbum(Album album_t) {
        try {
            connection = DBUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "insert into album (name,userid) values(?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, album_t.getName());
            preparedStatement.setInt(2, album_t.getUserid());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeResources();
    }

    /**
     * 根据id查询
     */
    public Album findByAlbumId(String id) throws SQLException {
        connection = DBUtil.getConnection();
        Album album = new Album();
        sql = "select name,userid from album where id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(id));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                album.setName(resultSet.getString("1"));
                album.setUserid(resultSet.getInt("2"));
                album.setId(Integer.parseInt(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return album;
    }
    /**
     * 查询所有
     */
    public ArrayList<Album> findAllAlbums(int location){
        ArrayList<Album> albumArrayList = new ArrayList<>();
        ArrayList<Photo> photoArrayList = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            sql = "select * from album limit ?,5";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, location);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Album album = new Album();
                album.setId(resultSet.getInt(1));
                album.setUserid(resultSet.getInt(2));
                album.setName(resultSet.getString(3));
                sql = "select albumid,photopath from photo where albumid = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, (album.getId()));
                resultSet_temp = preparedStatement.executeQuery();
                photoArrayList.clear();
                while(resultSet_temp.next()){
                    Photo photo = new Photo(Integer.parseInt(resultSet.getString(1)), resultSet.getString(2));
                    photoArrayList.add(photo);
                }
                album.setPhotos(photoArrayList);
                albumArrayList.add(album);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeParaResources();
        try {
            resultSet_temp.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albumArrayList;
    }
}
