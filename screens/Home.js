import { Button, Layout, Text } from '@ui-kitten/components'
import React from 'react'
import { useAuth } from '../contexts/AuthContext'

function Home() {
  const auth = useAuth();
  const signOut = () => {
    auth.signOut();
  }
  return (
    <Layout>
        <Text category='h1'>Home Screen</Text>
        <Button onPress={() => signOut()}>SignOut</Button>
    </Layout>
  )
}

export default Home