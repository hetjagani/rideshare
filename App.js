import { ApplicationProvider } from '@ui-kitten/components';
import Root from './Root';
import * as eva from '@eva-design/eva';

export default function App() {
  return (
    <ApplicationProvider {...eva} theme={eva.light}>
      <Root />
    </ApplicationProvider>
  );
}