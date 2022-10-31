import { ApplicationProvider } from "@ui-kitten/components";
import Root from "./Root";
import * as eva from "@eva-design/eva";
import { StripeProvider } from "@stripe/stripe-react-native";
import { STRIPE_PUBLISHABLE_KEY } from "./Config";

export default function App() {
  return (
    <StripeProvider
      publishableKey={STRIPE_PUBLISHABLE_KEY}
      merchantIdentifier="merchant.com.rideshare"
    >
      <ApplicationProvider {...eva} theme={eva.light}>
        <Root />
      </ApplicationProvider>
    </StripeProvider>
  );
}
