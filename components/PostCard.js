import { Text, Card, Button, Icon, Divider } from '@ui-kitten/components';
import React, { useRef, useState } from 'react';
import { Dimensions, Image, StyleSheet, View } from 'react-native';
import Carousel, { Pagination } from 'react-native-snap-carousel';
import { likePost, dislikePost } from '../services/postLike';
import Toast from 'react-native-toast-message';

const StarIcon = (props) => <Icon {...props} name="star" />;
const StarOutlineIcon = (props) => <Icon {...props} name="star-outline" />;

const Pill = ({ text, style }) => {
  return (
    <Text
      style={{
        ...style,
        borderColor: '#000',
        borderWidth: 1,
        borderRadius: 10,
        padding: 2,
      }}
      category="s2"
    >
      {text}
    </Text>
  );
};

const CardHeader = ({ text, postType }) => {
  const headerStyles = StyleSheet.create({
    layout: {
      display: 'flex',
      alignItems: 'flex-start',
      justifyContent: 'center',
    },
    title: {
      fontWeight: 'bold',
      marginHorizontal: 20,
      marginVertical: 10,
    },
  });
  return (
    <View style={headerStyles.layout}>
      <Text style={headerStyles.title} category="s1">
        {postType == 'RIDE' ? '🚗' : postType == 'RATING' ? '⭐' : '📜'} {text}
      </Text>
    </View>
  );
};

const CardFooter = ({ id, style, likes, liked, likedPost, dislikedPost }) => {
  const toggleLike = () => {
    if (liked) {
      dislikedPost(id);
    } else {
      likedPost(id);
    }
  };
  return (
    <View style={style}>
      <Button
        accessoryLeft={liked ? StarIcon : StarOutlineIcon}
        onPress={toggleLike}
      >
        {likes} Likes
      </Button>
    </View>
  );
};

const PostCard = ({ post, updateLike, navigation, shortView }) => {
  const [liked, setLiked] = useState(false);

  const cardStyle = StyleSheet.create({
    marginCard: {
      borderRadius: 6,
      elevation: 4,
      backgroundColor: '#fff',
      shadowOffset: {
        width: 1,
        height: 1,
      },
      shadowColor: '#333',
      shadowOpacity: 0.3,
      shadowRadius: 2,
      marginHorizontal: 10,
      marginVertical: 6,
    },
    cardContent: {
      display: 'flex',
    },
    footerLayout: {
      display: 'flex',
      alignItems: 'center',
    },
    description: {
      marginVertical: 5,
    },
    ride: {
      marginTop: 10,
    },
    rideTags: {
      flexDirection: 'row',
      alignItems: 'center',
      flexWrap: 'wrap',
      marginVertical: 5,
    },
  });

  const likedPost = (id) => {
    likePost(id)
      .then((res) => {
        setLiked(true);
        updateLike(id, post.noOfLikes + 1);
        Toast.show({
          type: 'success',
          text1: 'Liked a post',
        });
      })
      .catch((err) => {
        Toast.show({
          type: 'error',
          text1: 'Error in liking post',
        });
      });
  };

  const dislikedPost = (id) => {
    dislikePost(id)
      .then((res) => {
        setLiked(false);
        updateLike(id, post.noOfLikes - 1);
        Toast.show({
          type: 'success',
          text1: 'Disliked a post',
        });
      })
      .catch((err) => {
        Toast.show({
          type: 'error',
          text1: 'Error in disliking post',
        });
      });
  };

  const [index, setIndex] = useState(0);
  const isCarousel = useRef(null);
  const SLIDER_WIDTH = Dimensions.get('window').width + 30;
  const ITEM_WIDTH = Math.round(SLIDER_WIDTH * 0.8);
  const renderImageItem = ({ item, index }) => {
    return (
      <View
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <Image
          source={{ uri: item.url }}
          style={{
            width: Dimensions.get('window').width,
            resizeMode: 'contain',
            height: undefined,
            aspectRatio: 2,
          }}
        />
      </View>
    );
  };

  return (
    <Card
      style={cardStyle.marginCard}
      header={<CardHeader text={post.title} postType={post.type} />}
      footer={
        !shortView && (
          <CardFooter
            id={post.id}
            liked={liked}
            likes={post.noOfLikes}
            likedPost={likedPost}
            dislikedPost={dislikedPost}
            post={post}
            navigation={navigation}
          />
        )
      }
    >
      <View style={cardStyle.cardContent}>
        {/* Image Carousal */}
        {post.imageList && (
          <View
            style={{
              display: 'flex',
              alignItems: 'center',
            }}
          >
            <Carousel
              layout="default"
              ref={isCarousel}
              data={post.imageList}
              renderItem={renderImageItem}
              sliderWidth={SLIDER_WIDTH}
              itemWidth={ITEM_WIDTH}
              onSnapToItem={(index) => setIndex(index)}
              scrollEnabled={true}
              useScrollView={true}
              loopClonesPerSide={post.imageList?.length}
            />
            <Pagination
              dotsLength={post.imageList?.length}
              activeDotIndex={index}
              carouselRef={isCarousel}
              dotStyle={{
                width: 10,
                height: 10,
                borderRadius: 5,
                marginHorizontal: 8,
                backgroundColor: '#000',
              }}
              tappableDots={true}
              inactiveDotStyle={{
                backgroundColor: 'black',
              }}
              inactiveDotOpacity={0.4}
              inactiveDotScale={0.6}
            />
          </View>
        )}
        <Divider />
        {/* Description */}
        <View style={cardStyle.description}>
          <Text category="s1">{post.description}</Text>
        </View>

        {post.type == 'RIDE' && (
          // Ride Info
          <View>
            <Divider />

            <View style={cardStyle.ride}>
              {/* <Text category="s1">Ride:</Text> */}
              <Text>
                <Text category="s1">From:</Text>{' '}
                {post.ride?.startAddress?.street}
              </Text>
              <Text>
                <Text category="s1">To:</Text> {post.ride?.endAddress?.street}
              </Text>
              <Text>
                <Text category="s1">Price:</Text> ${post.ride?.pricePerPerson}
              </Text>
              <Text>
                <Text category="s1">Capacity:</Text>{' '}
                {post.ride?.noPassengers - post.ride?.capacity}/
                {post.ride?.noPassengers}
              </Text>
              <View style={cardStyle.rideTags}>
                {post.ride?.tags?.map((tag) => (
                  <Pill text={tag?.name} style={{ margin: 2 }} key={tag?.id} />
                ))}
              </View>
            </View>
          </View>
        )}
      </View>
    </Card>
  );
};

export default PostCard;
