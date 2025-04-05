import React, { useEffect, useState } from 'react';
import { RefreshControl, ScrollView } from 'react-native';
import PostCard from '../components/PostCard';
import getAuthData from '../contexts/getAuthData';
import fetchUserPosts from '../services/fetchUserPosts';

const MyPosts = ({ navigation }) => {
  const [postList, setPostList] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const [page, setPage] = useState(0);

  const onRefresh = React.useCallback(() => {
    setRefreshing(true);
    fetchPostsApi();
  }, []);

  const fetchPostsApi = () => {
    getAuthData()
      .then((authData) => {
        return fetchUserPosts({ page, limit: 10, userId: authData.userId });
      })
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
            navigation={navigation}
            shortView={true}
          />
        ))}
    </ScrollView>
  );
};

export default MyPosts;
