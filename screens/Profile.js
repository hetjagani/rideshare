import { Layout, Text } from "@ui-kitten/components";
import React, { useEffect, useState } from "react";
import { View, Image } from "react-native";
import { ProfileMenuItems } from "../components/ProfileMenuItems";
import { fetchUserDetails } from "../services/fetchUserDetails";
import LoadingView from "../components/loadingView";
import { USER_INFO_SCREEN } from "../routes/AppRoutes";

const Profile = ({ navigation }) => {
  const [name, setName] = useState("Temp Name");
  const [image, setImage] = useState("");
  const [loading, setLoading] = useState(false);
  const [rides, setRides] = useState(0);
  const [ratings, setRatings] = useState(0.0);
  const [months, setMonths] = useState(0.0);

  const getUserDetails = async () => {
    const res = await fetchUserDetails();
    if (res?.response?.status == 401) {
      Toast.show({
        type: "error",
        text1: "Unauthorised",
      });
      return;
    }

    if (res?.response?.status == 500) {
      Toast.show({
        type: "error",
        text1: "Error Fetching Details",
      });
      return;
    }

    if (res?.response?.status == 404) {
      navigation.navigate(USER_INFO_SCREEN);
    }

    const userName = res?.data?.firstName + " " + res?.data?.lastName;
    setName(userName);
    setImage(res?.data?.profileImage);
    setRatings(res?.data?.rating);
    setRides(res?.data?.rides);
    setMonths(res?.data?.months);
  };

  useEffect(() => {
    getUserDetails();
  }, []);

  return (
    <View style={{ height: "100%", backgroundColor: "white" }}>
      <Layout
        style={{ height: "40%", alignItems: "center", paddingTop: "10%" }}
      >
        <Layout
          style={{
            justifyContent: "center",
            backgroundColor: "white",
            borderRadius: "90%",
            height: 180,
            width: 180,
          }}
        >
          <Image
            onLoadStart={() => setLoading(true)}
            onLoadEnd={() => setLoading(false)}
            source={{ uri: image }}
            style={{
              borderRadius: "90%",
              height: 180,
              width: 180,
            }}
          />
          {loading && <LoadingView />}
        </Layout>
        <Layout style={{ marginTop: "3%" }}>
          <Text style={{ fontWeight: "bold", fontSize: "20px" }}>{name}</Text>
        </Layout>
        <View
          style={{
            marginTop: "2%",
            flexDirection: "row",
            alignItems: "center",
            width: "80%",
          }}
        >
          <View style={{ flex: 1, height: 1, backgroundColor: "black" }} />
        </View>
        <Layout
          style={{
            flexDirection: "row",
            marginTop: "2%",
            justifyContent: "space-around",
            width: "80%",
          }}
        >
          <Layout style={{ flexDirection: "column", alignItems: "center" }}>
            <Layout>
              <Text>{rides ?? 0}</Text>
            </Layout>
            <Layout>
              <Text>Rides</Text>
            </Layout>
          </Layout>
          <Layout style={{ flexDirection: "column", alignItems: "center" }}>
            <Layout>
              <Text>{ratings ?? 0.0}</Text>
            </Layout>
            <Layout>
              <Text>Ratings</Text>
            </Layout>
          </Layout>
          <Layout style={{ flexDirection: "column", alignItems: "center" }}>
            <Layout>
              <Text>{months ?? 0}</Text>
            </Layout>
            <Layout>
              <Text>Months</Text>
            </Layout>
          </Layout>
        </Layout>
      </Layout>
      <Layout style={{ height: "50%", marginTop: "15%" }}>
        <Layout style={{ marginTop: "5%", alignItems: "center" }}>
          <Text style={{ fontWeight: "bold" }}>Account Settings</Text>
        </Layout>
        <Layout>
          <ProfileMenuItems navigation={navigation} />
        </Layout>
      </Layout>
    </View>
  );
};

export default Profile;
