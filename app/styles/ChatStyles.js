import { StyleSheet } from "react-native";

export const ChatStyles = StyleSheet.create({
    container: {
        flex: 1,
        paddingLeft: 20,
        paddingRight: 20,
        alignItems: "center",
        backgroundColor: "#ffffff",
    },
    posttime: {
        fontSize: 12,
        color: "#666",
    },
    card: {
        width: "100%",
    },
    userinfo: {
        flexDirection: "row",
        justifyContent: "space-between",
    },
    userimgwrapper: {
        paddingTop: 15,
        paddingBottom: 15
    },
    userimg: {
        width: 50,
        height: 50,
        borderRadius: 25,
    },
    textsection: {
        flexDirection: "column",
        justifyContent: "center",
        padding: 15,
        paddingLeft: 0,
        marginLeft: 10,
        width: 300,
        borderBottomWidth: 1,
        borderBottomColor: "#cccccc",
    },
    userinfotext: {
        flexDirection: "row",
        justifyContent: "space-between",
        marginBottom: 5,
    },
    username: {
        fontSize: 14,
        fontWeight: "bold",
    },
    messagetext: {
        fontSize: 14,
        color: "#333333"
    } 
});
