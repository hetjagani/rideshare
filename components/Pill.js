import { Text } from '@ui-kitten/components';
import { StyleSheet } from 'react-native';

const Pill = ({ type, text, style }) => {
  const textStyle = StyleSheet.create({
    normal: {
      borderColor: 'blue',
      borderWidth: 1,
      borderRadius: 10,
      padding: 3,
      margin: 3,
      color: 'blue',
      fontSize: '16',
    },
    liked: {
      borderColor: 'green',
      borderWidth: 1,
      borderRadius: 10,
      padding: 3,
      margin: 3,
      color: 'green',
      fontSize: '16',
    },
    disliked: {
      borderColor: 'red',
      borderWidth: 1,
      borderRadius: 10,
      padding: 3,
      margin: 3,
      color: 'red',
      fontSize: '16',
    },
  });

  const v =
    type == 'NORMAL'
      ? textStyle.normal
      : type == 'LIKED'
      ? textStyle.liked
      : textStyle.disliked;

  return (
    <Text style={{ ...style, ...v }} category='s2'>
      {text}
    </Text>
  );
};

export default Pill;
