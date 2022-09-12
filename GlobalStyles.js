import { StyleSheet } from "react-native";

export const globalStyles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "white",
  },
  centeredContainer: {
    height: "100%",
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
  },
  input: {
    height: 50,
    width: 330,
    margin: 12,
    borderWidth: 1,
    borderColor: "#ffffff",
    padding: 10,
  },
  tinyLogo: {
    width: 200,
    height: 200,
  },
});