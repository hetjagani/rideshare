import React, { useEffect, useState } from 'react';
import {
  TabBar,
  Tab,
  Text,
  Card,
  List,
  Layout,
  Input,
} from '@ui-kitten/components';
import {
  StyleSheet,
  View,
  TouchableOpacity,
  KeyboardAvoidingView,
  Image,
} from 'react-native';
import { MESSAGE_SCREEN, ROOM_NAVIGATOR } from '../routes/AppRoutes';

import { Button, Modal, Divider } from '@ui-kitten/components';
import fetchMyRides from '../services/fetchMyRides';
import Toast from 'react-native-toast-message';
import Pill from '../components/Pill';
import Tags from 'react-native-tags';
import { Rating } from 'react-native-ratings';
import createRating from '../services/createRating';
import fetchRatingById from '../services/fetchRatingById';
import createRatingPost from '../services/createRatingPost';
import createRoom from '../services/createRoom';
import { fetchOtherUserDetails } from '../services/fetchOtherUserDetails';
import getAuthData from '../contexts/getAuthData';
import startRide from '../services/startRide';
import endRide from '../services/endRide';

const styles = StyleSheet.create({
  container: {
    maxHeight: '100%',
  },
  contentContainer: {
    paddingHorizontal: 8,
    paddingVertical: 4,
  },
  detailsModal: {
    display: 'flex',
    alignItems: 'center',
    width: '100%',
    paddingBottom: '50%',
  },
  backdrop: {
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  item: {
    marginVertical: 4,
  },
  detailsCard: {
    display: 'flex',
    justifyContent: 'space-between',
    width: '80%',
  },
});

const cardStatus = (status) => {
  return status === 'CREATED'
    ? 'primary'
    : status === 'ACTIVE'
    ? 'warning'
    : 'success';
};

const RidesForYou = ({ navigation }) => {
  const [rideListForYou, setRideListForYou] = useState([]);
  const [visibleRatingModal, setVisibleRatingModal] = useState(false);
  const [likedModal, setLikedModal] = useState([]);
  const [dislikedModal, setDislikedModal] = useState([]);
  const [ratingModal, setRatingModal] = useState(0);
  const [description, setDescription] = useState('');
  const [currentUser, setCurrentUser] = useState();

  const redirectToChatRoom = async (initiatedFor) => {
    const r = {
      initiatedFor,
    };

    const room = await createRoom(r);
    const usr = await fetchOtherUserDetails(initiatedFor);

    navigation.navigate(ROOM_NAVIGATOR, {
      screen: MESSAGE_SCREEN,
      initial: false,
      params: {
        userName: `${usr?.firstName} ${usr?.lastName}`,
        roomId: room?.id,
        senderId: parseInt(currentUser),
        receiverId: initiatedFor,
      },
    });
  };

  const addRatingPost = async (ratingsId) => {
    const existingRating = await fetchRatingById(ratingsId);
    const ratingPost = {
      title: `${existingRating?.data?.user?.firstName} ${existingRating?.data?.user?.lastName} was given ratings by ${existingRating?.data?.ratingUser?.firstName} ${existingRating?.data?.ratingUser?.lastName}.`,
      description: existingRating?.data?.description,
      refId: ratingsId,
      type: 'RATING',
      imageUrls: [],
    };

    createRatingPost(ratingPost)
      .then((res) => {
        Toast.show({
          type: 'success',
          text1: 'Rating Post created successfully',
        });
        getRidesForYou();
      })
      .catch((err) =>
        Toast.show({ type: 'error', text1: 'Unable to create Post' })
      );
  };

  const addRating = async (userId, rideId) => {
    const rat = {
      description: description,
      liked: likedModal,
      disliked: dislikedModal,
      rating: ratingModal,
      userId,
    };

    createRating(rat, rideId)
      .then((res) => {
        Toast.show({ type: 'success', text1: 'Ratings added successfully' });
        getRidesForYou();
        return res;
      })
      .catch((err) =>
        Toast.show({ type: 'error', text1: 'Unable to set Ratings' })
      );
  };

  const getRidesForYou = async () => {
    fetchMyRides()
      .then((res) => {
        setRideListForYou(
          res?.nodes?.filter((item) => {
            return item?.isPassenger == true;
          })
        );
      })
      .catch((err) =>
        Toast.show({
          type: 'error',
          text1: 'Enable to fetch rides For you.',
          text2: err,
        })
      );
  };

  const renderRidesForYouHeader = (headerProps, info) => (
    <View
      {...headerProps}
      style={{ marginTop: 8, marginLeft: 8, marginBottom: 8 }}
    >
      <View>
        <Text category="h6" style={{ fontWeight: 'bold' }}>
          Ride: #{info?.item?.id}
        </Text>
        <Text category="s1">
          <Text style={{ fontWeight: 'bold' }}>To:</Text>{' '}
          {info?.item?.endAddress?.street}, {info?.item?.endAddress?.city}
        </Text>
        <Text category="s1">
          <Text style={{ fontWeight: 'bold' }}>From:</Text>{' '}
          {info?.item?.startAddress?.street}, {info?.item?.startAddress?.city}
        </Text>
      </View>
    </View>
  );

  const renderRidesForYouFooter = (footerProps, info) => (
    <View
      {...footerProps}
      style={{
        padding: '2%',
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
      }}
    >
      <Button
        appearance="outline"
        status="primary"
        onPress={() => {
          redirectToChatRoom(info?.item?.userId);
        }}
      >
        CHAT
      </Button>

      <Layout>
        {info?.item?.status == 'COMPLETED' && (
          <Button
            appearance="outline"
            status="primary"
            onPress={() => setVisibleRatingModal(true)}
            disabled={info?.item?.isRatedByUser == true}
          >
            {info?.item?.isRatedByUser == true ? 'RATED' : 'RATE'}
          </Button>
        )}
        <Modal
          visible={visibleRatingModal}
          backdropStyle={styles.backdrop}
          onBackdropPress={() => {
            setVisibleRatingModal(false);
            setLikedModal([]);
            setDislikedModal([]);
            setRatingModal(0);
            setDescription('');
          }}
          style={styles.detailsModal}
        >
          <Card style={{ ...styles.detailsCard }}>
            <View style={{ justifyContent: 'center' }}>
              <Text
                category="h6"
                style={{ textAlign: 'center', fontWeight: 'bold' }}
              >
                New Rating
              </Text>
            </View>
            <Divider style={{ marginTop: '3%', marginBottom: '3%' }} />
            <Text category="s1" style={{ margin: 5, fontWeight: 'bold' }}>
              Liked:
            </Text>
            <Tags
              initialTags={likedModal}
              deleteTagOnPress={true}
              textInputProps={{
                placeholder: 'liked tags enter here',
              }}
              onChangeTags={(tags) => setLikedModal(tags)}
              containerStyle={{ justifyContent: 'center' }}
              inputStyle={{
                backgroundColor: 'white',
                borderColor: 'green',
                borderRadius: 50,
                borderWidth: 1,
                color: 'green',
                fontWeight: 'bold',
                fontSize: '16',
              }}
              renderTag={({ tag, index, onPress }) => (
                <TouchableOpacity key={`${tag}-${index}`} onPress={onPress}>
                  <Pill type={'LIKED'} text={tag} key={index} />
                </TouchableOpacity>
              )}
            />
            <Text category="s1" style={{ margin: 5, fontWeight: 'bold' }}>
              Disliked:
            </Text>
            <Tags
              initialTags={dislikedModal}
              deleteTagOnPress={true}
              textInputProps={{
                placeholder: 'disliked tags enter here',
              }}
              onChangeTags={(tags) => setDislikedModal(tags)}
              containerStyle={{ justifyContent: 'center' }}
              inputStyle={{
                backgroundColor: 'white',
                borderColor: 'red',
                borderRadius: 50,
                borderWidth: 1,
                color: 'red',
                fontWeight: 'bold',
                fontSize: '16',
              }}
              renderTag={({ tag, index, onPress }) => (
                <TouchableOpacity key={`${tag}-${index}`} onPress={onPress}>
                  <Pill type={'DISLIKED'} text={tag} key={index} />
                </TouchableOpacity>
              )}
            />
            <Text category="s1" style={{ margin: 5, fontWeight: 'bold' }}>
              Rating:
            </Text>
            <View style={{ alignItems: 'center' }}>
              <Rating
                imageSize={25}
                showRating
                ratingCount={5}
                fractions={1}
                jumpValue={0.1}
                onFinishRating={(rating) => {
                  setRatingModal(rating);
                }}
                style={{}}
              />
            </View>
            <Text category="s1" style={{ margin: 5, fontWeight: 'bold' }}>
              Description:
            </Text>
            <Input
              multiline={true}
              textStyle={{ maxHeight: 64 }}
              onChangeText={(input) => setDescription(input)}
              placeholder="Enter your description here"
            ></Input>
            <Button
              status="success"
              style={{ margin: 5, marginTop: '5%' }}
              onPress={() => {
                addRating(info?.item?.userId, info?.item?.id)
                  .then((res) => {
                    setVisibleRatingModal(false);
                    setLikedModal([]);
                    setDislikedModal([]);
                    setRatingModal(0);
                    setDescription('');
                  })
                  .catch((err) => err);
              }}
            >
              Submit
            </Button>
          </Card>
        </Modal>
      </Layout>

      {info?.item?.isRatedByUser == true && (
        <Button
          appearance="outline"
          status="primary"
          onPress={() => {
            addRatingPost(info?.item?.ratingsId);
          }}
        >
          POST
        </Button>
      )}
    </View>
  );

  const renderItem = (info) => (
    <Card
      style={styles.item}
      status={cardStatus(info?.item?.status)}
      header={(headerProps) => renderRidesForYouHeader(headerProps, info)}
      footer={(footerProps) => renderRidesForYouFooter(footerProps, info)}
    >
      <View style={{ marginTop: -9, marginLeft: -15 }}>
        <Text>
          <Text category="s1">Expected Start Time:</Text>{' '}
          {info?.item?.rideTime ? info?.item?.rideTime : 'N/A'}
        </Text>
        <Text>
          <Text category="s1">Ride started on:</Text>{' '}
          {info?.item?.startedAt
            ? new Date(info?.item?.startedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text>
          <Text category="s1">Ride ended on:</Text>{' '}
          {info?.item?.endedAt
            ? new Date(info?.item?.endedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text>
          <Text category="s1">Status:</Text>{' '}
          <Text style={{ color: '#3366ff', fontWeight: 'bold' }}>
            {info?.item?.status ? info?.item?.status : 'N/A'}
          </Text>
        </Text>
        <Text>
          <Text category="s1">Price:</Text>{' '}
          <Text style={{ fontWeight: 'bold' }}>
            ${info?.item?.pricePerPerson}
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
          {info?.item?.tags?.map((tag) => (
            <Pill type={'NORMAL'} text={tag?.name} key={tag?.id} />
          ))}
        </View>
      </View>
    </Card>
  );

  useEffect(() => {
    getAuthData((res) => {
      return res;
    })
      .then((user) => {
        setCurrentUser(user.userId);
      })
      .catch((err) => {
        console.log(err);
      });
    getRidesForYou();
  }, []);

  return (
    <List
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      data={rideListForYou}
      renderItem={renderItem}
    />
  );
};

const RidesByYou = () => {
  const [rideListByYou, setRideListByYou] = useState([]);

  const doStartRide = async (rideId) => {
    startRide(rideId)
      .then((res) => {
        console.log('start', res);
        Toast.show({
          type: 'success',
          text1: 'Ride Started',
        });
        getRidesByYou();
      })
      .catch((err) =>
        Toast.show({ type: 'error', text1: 'Cannot Start Ride' })
      );
  };

  const doEndRide = async (rideId) => {
    endRide(rideId)
      .then((res) => {
        Toast.show({
          type: 'success',
          text1: 'Ride Ended',
        });
        getRidesByYou();
      })
      .catch((err) => Toast.show({ type: 'error', text1: 'Cannot End Ride' }));
  };

  const getRidesByYou = async () => {
    fetchMyRides()
      .then((res) => {
        setRideListByYou(
          res?.nodes?.filter((item) => {
            return item?.isPassenger == false;
          })
        );
      })
      .catch((err) =>
        Toast.show({
          type: 'error',
          text1: 'Enable to fetch rides by you.',
          text2: err,
        })
      );
  };

  useEffect(() => {
    getRidesByYou();
  }, []);

  const renderRidesByYouHeader = (headerProps, info) => (
    <View
      {...headerProps}
      style={{ marginTop: 8, marginLeft: 8, marginBottom: 8 }}
    >
      <View>
        <Text category="h6" style={{ fontWeight: 'bold' }}>
          Ride: #{info?.item?.id}
        </Text>
        <Text category="s1">
          <Text style={{ fontWeight: 'bold' }}>To:</Text>{' '}
          {info?.item?.endAddress?.street}, {info?.item?.endAddress?.city}
        </Text>
        <Text category="s1">
          <Text style={{ fontWeight: 'bold' }}>From:</Text>{' '}
          {info?.item?.startAddress?.street}, {info?.item?.startAddress?.city}
        </Text>
      </View>
    </View>
  );

  const renderRidesByYouFooter = (footerProps, info) => (
    <View
      {...footerProps}
      style={{
        padding: '2%',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        width: '100%',
      }}
    >
      {info?.item?.requests && info?.item?.requests.length > 0 && (
        <View
          style={{
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            marginBottom: 15,
            width: '100%',
          }}
        >
          <Text category="s1">Ride Requested By: </Text>
          <View style={{ display: 'flex', flexDirection: 'row' }}>
            {info?.item?.requests.map((req) => (
              <View style={{ display: 'flex', alignItems: 'center', margin: 2, padding: 2 }}>
                <Image
                  style={{ height: 30, width: 30, borderRadius: 30 }}
                  source={
                    req?.user?.profileImage
                      ? { uri: req?.user?.profileImage }
                      : require('../assets/img_avatar.png')
                  }
                />
                <Text>
                  {req?.user?.firstName}
                </Text>
              </View>
            ))}
          </View>
        </View>
      )}
      <View
        style={{
          flexDirection: 'row',
          justifyContent: 'center',
          alignItems: 'center',
          width: '100%',
        }}
      >
        {Date.now() + 3600000 >= info?.item?.rideTime &&
          info?.item?.startedAt == null && (
            <Button
              appearance="outline"
              status="primary"
              onPress={() => doStartRide(info?.item?.id)}
            >
              START RIDE
            </Button>
          )}

        {info?.item?.status == 'ACTIVE' && (
          <Button
            appearance="outline"
            status="danger"
            onPress={() => doEndRide(info?.item?.id)}
          >
            END RIDE
          </Button>
        )}

        {info?.item?.status == 'COMPLETED' && (
          <View style={{ margin: '2%' }}>
            <Text style={{ fontWeight: 'bold' }}>Ride Completed</Text>
          </View>
        )}
      </View>
    </View>
  );

  const renderItemBy = (info) => (
    <Card
      style={styles.item}
      status={cardStatus(info?.item?.status)}
      header={(headerProps) => renderRidesByYouHeader(headerProps, info)}
      footer={(footerProps) => renderRidesByYouFooter(footerProps, info)}
    >
      <View style={{ marginTop: -9, marginLeft: -15 }}>
        <Text>
          <Text category="s1">Expected Start Time:</Text>{' '}
          {info?.item?.rideTime
            ? new Date(info?.item?.rideTime).toLocaleString('PST')
            : 'N/A'}
          {console.log(info?.item?.rideTime)}
        </Text>
        <Text>
          <Text category="s1">Ride started on:</Text>{' '}
          {info?.item?.startedAt
            ? new Date(info?.item?.startedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text>
          <Text category="s1">Ride ended on:</Text>{' '}
          {info?.item?.endedAt
            ? new Date(info?.item?.endedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text>
          <Text category="s1">Status:</Text>{' '}
          <Text style={{ color: '#3366ff', fontWeight: 'bold' }}>
            {info?.item?.status ? info?.item?.status : 'N/A'}
          </Text>
        </Text>
        <Text>
          <Text category="s1">Price:</Text>{' '}
          <Text style={{ fontWeight: 'bold' }}>
            ${info?.item?.pricePerPerson}
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
          {info?.item?.tags?.map((tag) => (
            <Pill type={'NORMAL'} text={tag?.name} key={tag?.id} />
          ))}
        </View>
      </View>
    </Card>
  );

  return (
    <List
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      data={rideListByYou}
      renderItem={renderItemBy}
    />
  );
};

const TopTabBar = ({ navigation, state }) => (
  <TabBar
    selectedIndex={state.index}
    onSelect={(index) => navigation.navigate(state.routeNames[index])}
    style={{ height: 50 }}
  >
    <Tab title="Ride By You" />
    <Tab title="Ride For You" />
  </TabBar>
);

export { RidesByYou, RidesForYou, TopTabBar };
