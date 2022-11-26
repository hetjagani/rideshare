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
} from 'react-native';
import { Button, Modal, Divider } from '@ui-kitten/components';
import fetchMyRides from '../services/fetchMyRides';
import Toast from 'react-native-toast-message';
import Pill from '../components/Pill';
import Tags from 'react-native-tags';
import { Rating } from 'react-native-ratings';
import createRating from '../services/createRating';
import fetchRatingById from '../services/fetchRatingById';
import createRatingPost from '../services/createRatingPost';
import { Keyboard } from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';

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

const RidesForYou = () => {
  const [rideListForYou, setRideListForYou] = useState([]);
  const [visibleRatingModal, setVisibleRatingModal] = useState(false);
  const [likedModal, setLikedModal] = useState([]);
  const [dislikedModal, setDislikedModal] = useState([]);
  const [ratingModal, setRatingModal] = useState(0);
  const [description, setDescription] = useState('');

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
        <Text category='h6' style={{ fontWeight: 'bold' }}>
          Ride: #{info?.item?.id}
        </Text>
        <Text category='s1'>
          <Text style={{ fontWeight: 'bold' }}>To:</Text>{' '}
          {info?.item?.endAddress?.street}, {info?.item?.endAddress?.city}
        </Text>
        <Text category='s1'>
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
      <Button appearance='outline' status='primary'>
        CHAT
      </Button>

      <Layout>
        {info?.item?.status == 'COMPLETED' && (
          <Button
            appearance='outline'
            status='primary'
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
                category='h6'
                style={{ textAlign: 'center', fontWeight: 'bold' }}
              >
                New Rating
              </Text>
            </View>
            <Divider style={{ marginTop: '3%', marginBottom: '3%' }} />
            <Text category='s1' style={{ margin: 5, fontWeight: 'bold' }}>
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
            <Text category='s1' style={{ margin: 5, fontWeight: 'bold' }}>
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
            <Text category='s1' style={{ margin: 5, fontWeight: 'bold' }}>
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
            <Text category='s1' style={{ margin: 5, fontWeight: 'bold' }}>
              Description:
            </Text>
            <Input
              multiline={true}
              textStyle={{ maxHeight: 64 }}
              onChangeText={(input) => setDescription(input)}
              placeholder='Enter your description here'
            ></Input>
            <Button
              status='success'
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
          appearance='outline'
          status='primary'
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
        <Text category='s3'>
          <Text category='s1'>Expected Start Time:</Text>{' '}
          {info?.item?.rideTime
            ? new Date(info?.item?.rideTime).toLocaleString
            : 'N/A'}
        </Text>
        <Text category='s3'>
          <Text category='s1'>Ride started on:</Text>{' '}
          {info?.item?.startedAt
            ? new Date(info?.item?.startedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text category='s3'>
          <Text category='s1'>Ride ended on:</Text>{' '}
          {info?.item?.endedAt
            ? new Date(info?.item?.endedAt).toLocaleString()
            : 'N/A'}
        </Text>
        <Text category='s3'>
          <Text category='s1'>Status:</Text>{' '}
          <Text style={{ color: '#3366ff', fontWeight: 'bold' }}>
            {info?.item?.status ? info?.item?.status : 'N/A'}
          </Text>
        </Text>
        <Text category='s3'>
          <Text category='s1'>Price:</Text>{' '}
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
  const [rideList, setRideList] = useState([]);
  const [displayRide, setDisplayRide] = useState({});
  const [visible, setVisible] = useState(false);

  const getMyRides = () => {
    fetchMyRides()
      .then((res) => {
        const responseList = res?.nodes;
        var listRides = [];
        for (const item of responseList) {
          if (item.isPassenger === false) {
            listRides.push(item);
          }
        }
        setRideList(listRides);
      })
      .catch((err) => {
        Toast.show({
          type: 'error',
          text1: 'Enable to fetch rides.',
          text2: err,
        });
      });
  };

  useEffect(() => {
    getMyRides(false);
  }, []);

  const openDetailsModal = ({ ride }) => {
    setDisplayRide(ride);
    setVisible(true);
  };

  const renderListItem = ({ item, index }) => {
    const styles = StyleSheet.create({
      container: {
        display: 'flex',
        borderRadius: 5,
        borderColor: '#444',
        backgroundColor: '#ddd',
        padding: 10,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        margin: 5,
      },
      tags: {
        flexDirection: 'row',
        alignItems: 'center',
        flexWrap: 'wrap',
        marginVertical: 5,
      },
    });

    const date = new Date(item?.createdAt);

    return (
      <View style={styles.container}>
        <View>
          <Text category='s1'>{`Ride to ${item?.endAddress?.street}`}</Text>
          <Text category='s2'>
            {date.toDateString()} - ${item.pricePerPerson}
          </Text>
          <View style={styles.tags}>
            {item?.tags?.map((tag) => (
              <Pill text={tag?.name} key={tag?.id} />
            ))}
          </View>
        </View>
        <View>
          <Button onPress={() => openDetailsModal({ ride: item })}>
            Details
          </Button>
        </View>
      </View>
    );
  };

  const DetailRow = ({ title, value }) => {
    const styles = StyleSheet.create({
      container: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        flexWrap: 'wrap',
      },
    });
    return (
      <View style={styles.container}>
        <Text category='s1'>{title}</Text>
        <Text
          category='p1'
          style={{ display: 'flex', flexWrap: 'wrap', overflow: 'scroll' }}
        >
          {value}
        </Text>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <List data={rideList} renderItem={renderListItem} />

      <Modal
        visible={visible}
        backdropStyle={styles.backdrop}
        onBackdropPress={() => setVisible(false)}
        style={styles.detailsModal}
      >
        <Card style={styles.detailsCard}>
          <DetailRow title={'Status:'} value={displayRide?.status} />
          <DetailRow
            title={'Price:'}
            value={`$${displayRide?.pricePerPerson}`}
          />
          <DetailRow
            title={'Capacity:'}
            value={`${displayRide?.noPassengers - displayRide?.capacity}/${
              displayRide?.noPassengers
            }`}
          />
          <DetailRow
            title={'Created At:'}
            value={new Date(displayRide?.createdAt).toDateString()}
          />
          <Divider />
          <DetailRow
            title={'Start Address:'}
            value={`${displayRide?.startAddress?.street}, ${displayRide?.startAddress?.line}, ${displayRide?.startAddress?.city} ${displayRide?.startAddress?.state}`}
          />
          <DetailRow
            title={'End Address:'}
            value={`${displayRide?.endAddress?.street}, ${displayRide?.endAddress?.line}, ${displayRide?.endAddress?.city} ${displayRide?.endAddress?.state}`}
          />
          <Divider />
          <View style={styles.tags}>
            {displayRide?.tags?.map((tag) => (
              <Pill text={tag?.name} key={tag?.id} />
            ))}
          </View>
        </Card>
      </Modal>
    </View>
  );
};

const TopTabBar = ({ navigation, state }) => (
  <TabBar
    selectedIndex={state.index}
    onSelect={(index) => navigation.navigate(state.routeNames[index])}
    style={{ height: 50 }}
  >
    <Tab title='Ride By You' />
    <Tab title='Ride For You' />
  </TabBar>
);

export { RidesByYou, RidesForYou, TopTabBar };
