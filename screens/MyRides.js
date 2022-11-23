import React, { useEffect, useState } from 'react';
import { StyleSheet, View } from 'react-native';
import {
  Text,
  List,
  Button,
  Modal,
  Card,
  Divider,
} from '@ui-kitten/components';
import fetchMyRides from '../services/fetchMyRides';
import Toast from 'react-native-toast-message';
import Pill from '../components/Pill';

const MyRides = () => {
  const [rideList, setRideList] = useState([]);
  const [displayRide, setDisplayRide] = useState({});
  const [visible, setVisible] = useState(false);

  const styles = StyleSheet.create({
    container: {
      margin: 10,
    },
    backdrop: {
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    detailsModal: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
    },
    detailsCard: {
      display: 'flex',
      justifyContent: 'space-between',
      width: '80%',
    },
    tags: {
      flexDirection: 'row',
      alignItems: 'center',
      flexWrap: 'wrap',
      marginVertical: 5,
    },
  });

  const getMyRides = () => {
    fetchMyRides()
      .then((res) => {
        setRideList(res?.nodes);
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
    getMyRides();
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
          <Text category="s1">{`Ride to ${item?.endAddress?.street}`}</Text>
          <Text category="s2">
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
        <Text category="s1">{title}</Text>
        <Text
          category="p1"
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

export default MyRides;
