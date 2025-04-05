import {
  Button,
  Card,
  Divider,
  Input,
  Layout,
  Spinner,
  Text,
} from '@ui-kitten/components';
import React, { useEffect, useRef, useState } from 'react';
import {
  Dimensions,
  Image,
  KeyboardAvoidingView,
  StyleSheet,
  TouchableOpacity,
  View,
} from 'react-native';
import * as ImagePicker from 'expo-image-picker';
import { RNS3 } from 'react-native-aws3';
import uuid from 'react-native-uuid';
import {
  AWS_ACCESS_KEY,
  AWS_BUCKET,
  AWS_REGION,
  AWS_SECRET_KEY,
} from '../Config';
import Carousel, { Pagination } from 'react-native-snap-carousel';
import { ADD_POST_RIDE_SCREEN, HOME_SCREEN } from '../routes/AppRoutes';
import { addPost } from '../services/addPost';
import { useIsFocused } from '@react-navigation/native';
import Pill from '../components/Pill';

const LoadingIndicator = (props) => (
  <View style={[props.style]}>
    <Spinner size="small" />
  </View>
);

const AddPost = ({ navigation, route }) => {
  const styles = StyleSheet.create({
    container: {
      display: 'flex',
      height: '100%',
      alignItems: 'center',
      padding: 10,
      justifyContent: 'space-around',
    },
    postForm: {
      width: '80%',
      marginTop: 30,
    },
    postInput: {
      marginTop: 20,
    },
    saveButton: {
      width: '80%',
    },
    item: {
      marginVertical: 4,
    },
  });

  const [isUploading, setIsUploading] = useState(false);
  const [postType, setPostType] = useState('DESC');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [images, setImages] = useState([]);
  const [ride, setRide] = useState({});

  const isFocused = useIsFocused();

  useEffect(() => {
    if (route?.params?.refId) {
      setPostType('RIDE');
      // get the ride and show ride details
      setRide(route?.params?.ride);
    }
  }, [isFocused]);

  const pickImage = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    if (!result.cancelled) {
      uploadImageOnS3(result);
    }
  };

  const deleteImage = () => {
    const copyImages = images;
    if (copyImages.length == 1) {
      setImages([]);
      return;
    }
    copyImages.splice(index, 1);
    setImages(copyImages);
    setIndex(0);
  };

  const uploadImageOnS3 = async (image) => {
    const options = {
      keyPrefix: '',
      bucket: AWS_BUCKET,
      region: AWS_REGION,
      accessKey: AWS_ACCESS_KEY,
      secretKey: AWS_SECRET_KEY,
      successActionStatus: 201,
    };
    const file = {
      uri: `${image.uri}`,
      name: uuid.v4(),
      type: image.type,
    };

    return new Promise((resolve, reject) => {
      RNS3.put(file, options)
        .progress((e) => {
          setIsUploading(true);
          console.log(e.loaded / e.total);
        })
        .then((res) => {
          if (res.status === 201) {
            setIsUploading(false);
            const { postResponse } = res.body;
            setImages([...images, postResponse.location]);
            resolve({
              src: postResponse.location,
            });
          } else {
            alert('Error Uploading Image, try again');
            console.log('error uploading to s3', res);
          }
        })
        .catch((err) => {
          alert('Error Uploading Image, try again');
          console.log('error uploading to s3', err);
          reject(err);
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
          source={{ uri: item }}
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

  const savePost = () => {
    const data = {
      description,
      title,
      imageUrls: images,
      type: postType,
      refId: route?.params?.refId,
    };
    addPost(data).then((res) => {
      if (res?.response && res?.response.status != 200) {
        Toast.show({
          type: 'error',
          text1: 'Error Adding Post',
          text2: res?.response?.data?.message,
        });
        return;
      }

      navigation.navigate(HOME_SCREEN);
    });
  };

  const cardStatus = (status) => {
    return status === 'CREATED'
      ? 'primary'
      : status === 'ACTIVE'
      ? 'warning'
      : 'success';
  };
  return (
    <KeyboardAvoidingView behavior="position">
      <Layout style={styles.container}>
        <Text category="h1">Post Details</Text>
        <View style={styles.postForm}>
          {images && images.length > 0 && (
            <View
              style={{
                display: 'flex',
                alignItems: 'center',
              }}
            >
              <Carousel
                layout="default"
                ref={isCarousel}
                data={images}
                renderItem={renderImageItem}
                sliderWidth={SLIDER_WIDTH}
                itemWidth={ITEM_WIDTH}
                onSnapToItem={(index) => setIndex(index)}
                scrollEnabled={true}
                useScrollView={true}
                loopClonesPerSide={images?.length}
              />
              <Pagination
                dotsLength={images?.length}
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
              <Divider />
            </View>
          )}
          <Input
            value={title}
            label={'Title'}
            placeholder="Post Title"
            onChangeText={(next) => setTitle(next)}
            style={styles.postInput}
          />
          <Input
            value={description}
            label={'Description'}
            placeholder="Post Description"
            onChangeText={(next) => setDescription(next)}
            style={styles.postInput}
            multiline={true}
            textStyle={{ minHeight: 64 }}
          />
          <TouchableOpacity
            disabled={route?.params?.refId != null}
            style={styles.postInput}
            onPress={() => navigation.navigate(ADD_POST_RIDE_SCREEN)}
          >
            <Text category="s1" style={{ color: 'blue' }}>
              Add Ride in your post
            </Text>
          </TouchableOpacity>
        </View>
        <View
          style={{
            ...styles.postInput,
            display: 'flex',
            justifyContent: 'space-between',
            flexDirection: 'row',
            width: '80%',
          }}
        >
          <Button
            style={{ margin: 2, width: '50%' }}
            onPress={pickImage}
            accessoryLeft={isUploading === true && LoadingIndicator}
            appearance={isUploading === true ? 'outline' : 'filled'}
          >
            Add Post Image
          </Button>
          <Button
            status="danger"
            style={{ margin: 2, width: '50%' }}
            onPress={deleteImage}
            disabled={images.length == 0 || isUploading === true}
          >
            Delete Image
          </Button>
        </View>

        {route?.params?.refId && (
          <Card
            style={styles.item}
            status={cardStatus(ride?.status)}
            // header={(headerProps) => renderRidesByYouHeader(headerProps, info)}
            // footer={(footerProps) => renderRidesByYouFooter(footerProps, info)}
          >
            <View style={{ marginTop: -9, marginLeft: -15 }}>
              <Text>
                <Text category="s1">Expected Start Time:</Text>{' '}
                {ride?.rideTime
                  ? new Date(ride?.rideTime).toLocaleString('PST')
                  : 'N/A'}
              </Text>
              <Text>
                <Text category="s1">Ride started on:</Text>{' '}
                {ride?.startedAt
                  ? new Date(ride?.startedAt).toLocaleString()
                  : 'N/A'}
              </Text>
              <Text>
                <Text category="s1">Ride ended on:</Text>{' '}
                {ride?.endedAt
                  ? new Date(ride?.endedAt).toLocaleString()
                  : 'N/A'}
              </Text>
              <Text>
                <Text category="s1">Status:</Text>{' '}
                <Text style={{ color: '#3366ff', fontWeight: 'bold' }}>
                  {ride?.status ? ride?.status : 'N/A'}
                </Text>
              </Text>
              <Text>
                <Text category="s1">Price:</Text>{' '}
                <Text style={{ fontWeight: 'bold' }}>
                  ${ride?.pricePerPerson}
                </Text>
              </Text>
              <View
                style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  marginVertical: 5,
                  marginBottom: -5,
                }}
              >
                {ride?.tags?.map((tag) => (
                  <Pill type={'NORMAL'} text={tag?.name} key={tag?.id} />
                ))}
              </View>
            </View>
          </Card>
        )}

        <View style={styles.saveButton}>
          <Button onPress={() => savePost()} status="success">
            Save Post
          </Button>
        </View>
      </Layout>
    </KeyboardAvoidingView>
  );
};

export default AddPost;
