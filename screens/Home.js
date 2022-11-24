import React, { useCallback, useEffect, useState } from 'react';
import {
  ScrollView,
  RefreshControl,
  FlatList,
  ActivityIndicator,
  View,
} from 'react-native';
import PostCard from '../components/PostCard';
import fetchPosts from '../services/fetchPosts';
import Toast from 'react-native-toast-message';

function Home({ navigation }) {
  const [postList, setPostList] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const [page, setPage] = useState(0);

  const onRefresh = useCallback(() => {
    setPostList([]);
    setRefreshing(true);
    setPage(0);
  }, []);

  const fetchPostsApi = () => {
    fetchPosts({ page, limit: 2 })
      .then((resp) => {
        if (resp?.nodes.length > 0) {
          setPostList([...postList, ...resp?.nodes]);
          setRefreshing(false);
        }
      })
      .catch((err) => {
        Toast.show({
          type: 'error',
          text1: 'Enable to fetch posts.',
          text2: err,
        });
      });
  };

  const updateLike = (id, likes) => {
    const newList = postList.map((post) => {
      if (post.id == id) {
        post.noOfLikes = likes;
      }
      return post;
    });

    setPostList(newList);
  };

  useEffect(() => {
    fetchPostsApi();
  }, [page]);

  const renderItem = ({ item: post }) => {
    return (
      <PostCard
        post={post}
        key={post?.id}
        updateLike={updateLike}
        navigation={navigation}
      />
    );
  };

  const renderLoader = () => {
    return (
      <View>
        <ActivityIndicator
          size="large"
          color="#aaa"
          style={{ marginVertical: 16, alignItems: 'center' }}
        />
      </View>
    );
  };

  const loadMoreItems = () => {
    setPage(page + 1);
  };

  return (
    <FlatList
      data={postList}
      renderItem={renderItem}
      keyExtractor={(item) => item?.id}
      refreshControl={
        <RefreshControl
          tintColor={'#aaa'}
          onRefresh={onRefresh}
          refreshing={refreshing}
        />
      }
      ListFooterComponent={renderLoader}
      onEndReached={loadMoreItems}
      onEndReachedThreshold={0.8}
    />
  );
}

export default Home;
