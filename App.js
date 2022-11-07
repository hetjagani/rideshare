import { ApplicationProvider, IconRegistry } from "@ui-kitten/components";
import Root from "./Root";
import * as eva from "@eva-design/eva";
import { StripeProvider } from "@stripe/stripe-react-native";
import { STRIPE_PUBLISHABLE_KEY } from "./Config";
import { EvaIconsPack } from "@ui-kitten/eva-icons";
import Toast from "react-native-toast-message";

export default function App() {
  return (
    <>
      <IconRegistry icons={EvaIconsPack} />
      <StripeProvider
        publishableKey={STRIPE_PUBLISHABLE_KEY}
        merchantIdentifier="merchant.com.rideshare"
      >
        <ApplicationProvider {...eva} theme={eva.light}>
          <Root />
        </ApplicationProvider>
      </StripeProvider>
      <Toast />
    </>
  );
}
