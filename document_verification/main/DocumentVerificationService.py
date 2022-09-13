import idanalyzer

try:
    # Initialize Core API with your api key and region (US/EU)
    coreapi = idanalyzer.CoreAPI("tkxvvEQqnxMHZaLKbsCRKMy8YUHPBUsL", "US")

    # Raise exceptions for API level errors
    # coreapi.throw_api_exception(True)
    
    # enable document authentication using quick module
    coreapi.enable_authentication(True, 'quick')

    # perform scan
    response = coreapi.scan(document_primary="Sahil.jpg")

    # All the information about this ID will be returned in response dictionary
    print(response)

    # Print document holder name
    if response.get('result'):
        data_result = response['result']
        print("Hello your name is {} {}".format(data_result['firstName'],data_result['lastName']))

    # Parse document authentication results
    if response.get('authentication'):
        authentication_result = response['authentication']
        if authentication_result['score'] > 0.5:
            print("The document uploaded is authentic")
        elif authentication_result['score'] > 0.3:
            print("The document uploaded looks little bit suspicious")
        else:
            print("The document uploaded is fake")

    # Parse biometric verification results

except idanalyzer.APIError as e:
    # If API returns an error, catch it
    details = e.args[0]
    print("API error code: {}, message: {}".format(details["code"], details["message"]))
    
except Exception as e:
    print(e)