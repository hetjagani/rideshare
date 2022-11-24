import React, { useEffect, useState } from 'react';
import { ScrollView, RefreshControl } from 'react-native';
import PostCard from '../components/PostCard';
import fetchPosts from '../services/fetchPosts';
import Toast from 'react-native-toast-message';

function Home({ navigation }) {
  const [postList, setPostList] = useState([]);
  const [refreshing, setRefreshing] = React.useState(false);

  const onRefresh = React.useCallback(() => {
    setRefreshing(true);
    fetchPostsApi();
  }, []);

  const [page, setPage] = useState(0);

  const fetchPostsApi = () => {
    fetchPosts({ page, limit: 10 })
      .then((resp) => {
        setPostList(resp?.nodes);
        setRefreshing(false);
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
  }, []);

  return (
    <ScrollView
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      {postList &&
        postList.map((post) => (
          <PostCard
            post={post}
            key={post?.id}
            updateLike={updateLike}
            navigation={navigation}
          />
        ))}
    </ScrollView>
  );
}

export default Home;
