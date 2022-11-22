import { Text } from '@ui-kitten/components';

const Pill = ({ text, style }) => {
  return (
    <Text
      style={{
        ...style,
        borderColor: '#000',
        borderWidth: 1,
        borderRadius: 10,
        padding: 2,
        margin: 2,
      }}
      category="s2"
    >
      {text}
    </Text>
  );
};

export default Pill;
