# Valentuesday-mobile

An app for valentines day (🇩🇪: Valentinstag -> Valendienstag).

Create questions with the (soon-to-be) integrated question creator for your SO and have them answer your questions!

## How it currently works

### Create questions for your SO

Be an admin of the (soon-to-be-hosted) [Valentuesday Backend](https://github.com/Z-100/valentuesday-backend) and 
do some stuff with e.g. Swagger or Postman by sending HTTP requests to the server.

**Stuff means the following:**

1. Create an account (for your SO to use)
    - Only `username` & `password` are needed
    - The rest will be generated for you
    - Up on creation you'll recieve a `Bearer-Token`
2. Add questions to the account!
    - Each question has a `question`, three `answers` & one `solution`
    - Send the `Bearer Token` with each request
3. Done; The only thing left to do, is sending the activation key to your SO
    - Get the `activationKey` by sending another request
4. Additionally: If you already created an account, just log in to receive the `Bearer-Token`

### Answer the questions

After logging into the app using your `activationKey` your SO has to answer your questions... Yeah.
