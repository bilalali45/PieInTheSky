package pk.com.Taj.app;

import org.json.JSONException;
import org.json.JSONObject;

public class Constant {
   public static JSONObject jsonObject_Tajhotel;

    static {
        try {
            jsonObject_Tajhotel = new JSONObject("{\n" +
                    " \"MenuList\":[\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"cd0dac3b-7a92-4255-8473-863d6869ca0f\",\n" +
                    "\t\t\t\"CategoryName\": \"Starters\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"0e42766d-673f-4151-aecf-361c8b6fceba\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Loaded Fries\",\n" +
                    "\t\t\t\t\t\"Description\":\"French fries loaded with chunks of chicken, Jalapeno, red onion topped with delicious cheese sauce\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 392.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"4d3abb76-4123-4fba-8cab-12438e428f5d\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Honey Chicken Wings\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Honey-Chicken-Wings.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 372.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"76c01cdb-8f5e-4814-a206-d048abfff02e\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Spring Rolls\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken Spring Rolls are light and crispy filled with chicken & seasonal vegetables\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Chicken-Spring-Rolls.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 392.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"04c6428a-2835-4f4b-9223-123f1484dbed\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Dhaka Chicken\",\n" +
                    "\t\t\t\t\t\"Description\":\"Boneless Chicken, Red Chili, Black Pepper, Green Chili, Cornstarch, Sesame Seeds, Deep Fried\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Dhaka-Chicken.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 504.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e5fdc573-4dd5-46da-875c-1c653d90ba81\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Dynamite Chicken\",\n" +
                    "\t\t\t\t\t\"Description\":\"Succulent chicken cubes butter fried tossed in dynamite sauce\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Dynamite-Chicken.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 552.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e85ca981-8517-4f49-b4e5-ba26cc84b5a7\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Drum Sticks\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken mince with onion, green pepper, black pepper rolled on drumsticks, coated with our in house batter and fried to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Chicken-Drum-Sticks.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 552.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"485da5ef-6582-4b14-88a2-0e5310b81a45\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Dhaka Fish\",\n" +
                    "\t\t\t\t\t\"Description\":\"Boneless Fish, Lemon, Green Pepper, Ginger Garlic Paste, Fine Flour, Cornstarch coated with Sesame seed and fried to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Dhaka-Fish.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 584.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"15561c4b-a49c-4258-8e53-d2d434b82b5d\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Prawn Tempura\",\n" +
                    "\t\t\t\t\t\"Description\":\"Fried tempura prawns are lightly coated with flavorful batter and fried to perfection served with sweet chili dipping sauce\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Prawn-Tempura.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 952.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"808f44b9-c2ff-430e-84b7-0904cc522887\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Dynamite Prawns\",\n" +
                    "\t\t\t\t\t\"Description\":\"Scrumptious crispy deep fried prawns tossed in creamy, spicy, tangy mayo sauce\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Dynamite-Prawns.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 944.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e708dbac-61ed-4819-a099-772d7c88ef36\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Prawn Balls\",\n" +
                    "\t\t\t\t\t\"Description\":\"Finger licking deep fried prawn balls, crispy from the outside, soft and delicious from inside\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Prawn-Balls.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 760.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"fceb8bbf-1456-41f3-9560-baaae28b0bbf\",\n" +
                    "\t\t\t\t\t\"DishName\": \"French Fries\",\n" +
                    "\t\t\t\t\t\"Description\":\"Satisfy your snack cravings with perfectly cooked crunchy fries\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"French-Fries.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 232.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"33bd9fc7-0f3b-43e3-b24b-17b6efd34845\",\n" +
                    "\t\t\t\t\t\"DishName\": \"RT Special Platter\",\n" +
                    "\t\t\t\t\t\"Description\":\"(for 4 person) (Dynamite chicken, spring roll, boneless Nana wings & Dhaka fish)\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"RT-Special-Platter.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 1192.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"33bd9fc7-0f3b-43e3-b24b-17b6efd34845\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Tandoori Prawn Masala\",\n" +
                    "\t\t\t\t\t\"Description\":\"Utterly moreish, creamy, smoky tandoori prawn masala is a fragrant medley of fresh spices and aromatics, prepared with juicy jumbo prawns in a rich tangy gravy\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Tandoori-Prawn-Masala.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 920.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"f90b8e04eaad44cab4d70c9c283e05d9\",\n" +
                    "\t\t\t\"CategoryName\": \"Soups\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"ee853c83-de0d-49d3-8588-2271698aa8ae\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Corn Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"Traditional soup prepared with our homemade chicken stock, fine Julien cut chicken, egg and sweet corn\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Chicken-Corn-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 216.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"2b67c47f-e4c1-490e-9386-efffaaeb2b70\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Hot & Sour Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"Soup prepared with Chicken stock, spices, fine Julien cut chicken & Vegetables\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Hot-&-Sour-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 216.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"c4539d0f-8839-4696-833c-a67c1f05132c\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Hot Szechuan Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"A hearty soup from Szechuan region of western China\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Hot-Szechuan-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 216.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"75a29f01-560b-4af8-adc2-f9e54c9abbb0\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Cream of Mushroom\",\n" +
                    "\t\t\t\t\t\"Description\":\"A thick creamy soup, a delight for people who prefer thick soups with crusty bread\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Cream-of-Mushroom.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 232.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"7126279d-bb1f-4903-b7db-c6417c73c44f\",\n" +
                    "\t\t\t\t\t\"DishName\": \"RT Special Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"Delicious thick soup with chicken, fish, mushrooms and blend of Vegetables\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"RT-Special-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 216.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"6833052a-4b36-4c96-b8ef-4836405816a8\",\n" +
                    "\t\t\t\"CategoryName\": \"Bar B.Q\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"bd9d54ca-dc3f-4e39-8c79-ff7242dfb4fb\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Cheesy Chicken Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"A delicious and mouth watering combination of cheese and chicken - a must have!\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 596.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"f71364ee-a43c-4bd0-a0c8-9e6c13d0e9c0\",\n" +
                    "\t\t\t\t\t\"DishName\": \"BBQ Grilled Prawn\",\n" +
                    "\t\t\t\t\t\"Description\":\"Crispy grilled prawns served with our signature condiments\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 1160.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"c4dedc3e-fa22-41f5-8c53-7724694637cc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Tikka\",\n" +
                    "\t\t\t\t\t\"Description\":\"A spicy piece of chicken with bones grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 296.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"cce14a49-a596-4f6d-bdce-159331c53e74\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Persian Beef Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Iranian meat kebab made with ground beef mixed with ground pepper and chopped onions\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 696.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e143fc82-22a8-48ca-b525-5d7c306a9fe2\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Turkish Mutton Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Turkish mutton kebabs marinated with Turkish spices, white onion, ground cumin and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 1000.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"70a84a45-48df-41cf-8c1b-75654ae0b287\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Shish Taouk Chicken\",\n" +
                    "\t\t\t\t\t\"Description\":\"Shish Taouk grilled chicken served with aromatic rice\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no-image.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 632.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e28b812c-fbdf-4589-97be-c38a6f3b664e\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Reshmi Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Soft creamy chicken kebabs, adequately seasoned with spices and herbs and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Reshmi-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 476.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"d162562f-e75d-496e-830f-d6937c0cbc3d\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Malai Chicken Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken cubes, Puck cheese, White Pepper, Green Pepper, with Special Spices, its grilled, its creamy and its delicious\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Malai-Chicken-Boti.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 616.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"52a76523-80a0-444b-8232-bbd5c3169171\",\n" +
                    "\t\t\t\t\t\"DishName\": \"RT Special Kalmi Tikka (6 Pieces)\",\n" +
                    "\t\t\t\t\t\"Description\":\"Leg Pieces of Chicken with Cream, White Pepper, Black Pepper, and Green Pepper\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"RT-Special-Kalmi-Tikka-(6 Pieces).jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 680.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"095859a7-99bb-4a28-a80d-b2d76d403ec7\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Fish Tikka (8 Pieces)\",\n" +
                    "\t\t\t\t\t\"Description\":\"Fish cubes marinated in yogurt, ginger Garlic paste with mustard oil with our special blend of spices and grilled on charcoal to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Fish-Tikka-(8 Pieces).jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 632.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"fb6835e9-f333-407b-8544-474903ddf9ad\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bihari Chicken Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken boneless marinated overnight in yogurt, onion, special Bihari Spices and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Bihari-Chicken-Boti.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 584.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"7a1e194d-7036-41bb-9826-739141353032\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Haryali Chicken Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"Delicious juicy chicken cubes marinated in a paste of fresh coriander, green chilies, mint and fragrant spices for a distinctive flavor\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Haryali-Chicken-Boti.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 600.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"1e4488c9-834d-4697-abe2-2c918a18e731\",\n" +
                    "\t\t\t\t\t\"DishName\": \"RT Kabab Platter\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken seekh kababs marinated in Pakistani herbs and spices and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"RT-Kabab-Platter.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 1272.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"622314e1-0d57-4efc-81c1-84434b3d1886\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Royal Grilled Platter\",\n" +
                    "\t\t\t\t\t\"Description\":\"This platter includes mutton leg roast, prawn tikka, fish tikka, malai boti, chicken tikka boti, reshmi kabab, chicken dum seekh, afghani pulao, BBQ Sauce, Naan, Salad & Raita(4-5 Person Serving)\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Royal-Grilled-Platter.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 3192.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"9a49bc41-7f00-4719-a651-8ef233399e7a\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Mutton Seekh Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Delicious mutton seekh kababs prepared with blend of flavorful spices\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Mutton-Seekh-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 952.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"a989de50-3b3e-4430-b602-1bd54f511caa\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Achari Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken boneless marinated in yogurt, achari spices, green chilies, lemon and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Chicken-Achari-Boti.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 584.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"bbf25809-ddf5-4e3e-a604-d09bd3819c90\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Tikka Boti\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken with bones marinated in yogurt, vinegar, spices and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Chicken-Tikka-Boti.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 584.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"599f2515-bb84-4c22-9fb4-e56b493e01ac\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Kesari Mutton Chops\",\n" +
                    "\t\t\t\t\t\"Description\":\"Delicious mouth watering mutton chops prepared with blend of flavorsome spices and saffron\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Kesari-Mutton-Chops.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 984.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"400b910a-c53d-4d8c-ad57-ba7c2e644a89\",\n" +
                    "\t\t\t\t\t\"DishName\": \"RT Khas Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken seekh kabab marinated in Pakistani herbs and spices and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"RT-Khas-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 616.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"b12e6e79-45b3-4629-b255-5319a488d185\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Behari Chicken Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken minced, onion, green pepper, paprika powder, tomato puree and Behari spices\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Behari-Chicken-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 584.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"a32af512-122f-4039-bc46-291fc5620bc6\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Beef Seekh Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Extremely flavorful, beef kababs are made with smooth mixture of beef mince, seasoned with freshly ground spices & herbs and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Beef-Seekh-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 632.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"497cf7fe-7a60-4c94-a793-8f1dedd36b48\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Beef Behari Kabab\",\n" +
                    "\t\t\t\t\t\"Description\":\"Extremely flavorful, beef kababs are made with smooth mixture of beef mince, seasoned with freshly ground spices & herbs and grilled to perfection\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Beef-Behari-Kabab.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 664.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"8057f64e-7e14-44b3-9da2-a1164cd09cee\",\n" +
                    "\t\t\t\"CategoryName\": \"Chinese Cuisine\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"53f26dd7-80cc-4a34-9539-55dc75b8b625\",\n" +
                    "\t\t\t\"CategoryName\": \"Salads\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"993ad7cf-76ac-4ad5-9d7c-903aeafa38f2\",\n" +
                    "\t\t\t\"CategoryName\": \"Pakistani Curries Mutton\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"516b2f05-dc62-4760-8a0d-bdd46407270b\",\n" +
                    "\t\t\t\"CategoryName\": \"Pakistani Curries Chicken\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"490efb24-db7d-4f05-90a2-aa2b5cfeda13\",\n" +
                    "\t\t\t\"CategoryName\": \"Handi\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"07bfe3b9-3831-4cdc-87b7-de26d51bb6c9\",\n" +
                    "\t\t\t\"CategoryName\": \"Fast Food\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"c4a45978-4cdc-46d1-9070-4189825f53c8\",\n" +
                    "\t\t\t\"CategoryName\": \"Vegetable & Daal\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"896d7a62-c73c-4978-baa7-0d991acf1782\",\n" +
                    "\t\t\t\"CategoryName\": \"Rice / Noodles\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"e740124b-3780-418e-a8dd-b387a7e742bc\",\n" +
                    "\t\t\t\"CategoryName\": \"Tandoor\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"498b9f2b-790d-4001-83fa-2422a7659db2\",\n" +
                    "\t\t\t\"CategoryName\": \"Desserts\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"6db41e84-bc6a-4357-9d92-537ab8c87df4\",\n" +
                    "\t\t\t\"CategoryName\": \"Cold Beverages\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"7fac2f73-c052-4a76-9377-6ae6db4ac029\",\n" +
                    "\t\t\t\"CategoryName\": \"Hot Beverages\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"cbfee71c-de3f-4c72-a3c0-74f96706e664\",\n" +
                    "\t\t\t\"CategoryName\": \"Snacks-Khi\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"182373ae-d808-4e1d-aeaf-8de9662105ee\",\n" +
                    "\t\t\t\"CategoryName\": \"Pizza\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"e01a7ec5-f9d3-41be-806b-3990930042e8\",\n" +
                    "\t\t\t\"CategoryName\": \"Fiesta Deals\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\t\t\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"e808c35c-dd38-4e03-9752-a82d61d61bec\",\n" +
                    "\t\t\t\"CategoryName\": \"Continental-Karachi\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"18813d69-c0ff-4a36-90f1-988a8cc428dc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Bagel With Cream Cheese\",\n" +
                    "\t\t\t\t\t\"Description\":\"Wings are cooked to precision in Ginger, Garlic, Soya Sauce, WOK Sauce, Honey and blend of spices, topped with white Sesame Seeds\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"http://pitspos.com/Content/assets/images/no-image-icon.png\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 420.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t}\t\t\n" +
                    "\t]\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject jsonObject_yelohotel;
    static {
        try {
            jsonObject_yelohotel = new JSONObject("{\n" +
                    " \"MenuList\":[\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"6eef1ec1-8355-4cb6-8857-695aaf6b56bc\",\n" +
                    "\t\t\t\"CategoryName\": \"Ice cream\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"57970f6c-e17e-4b8f-ae11-1ed21d970a6a\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Yelo\",\n" +
                    "\t\t\t\t\t\"Description\":\"Imported French Bread layered in garlic butter and served with a distinct marinara sauce.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"butter-garlic-bread.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 135.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t]\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\t]\n" +
                    "}");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static JSONObject jsonObject_paittoohotel;
    static {
        try {
            jsonObject_paittoohotel = new JSONObject("{\n" +
                    " \"MenuList\":[\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"6eef1ec1-8355-4cb6-8857-695aaf6b56bc\",\n" +
                    "\t\t\t\"CategoryName\": \"Appetizers\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"57970f6c-e17e-4b8f-ae11-1ed21d970a6a\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Buttery Garlic Bread\",\n" +
                    "\t\t\t\t\t\"Description\":\"Imported French Bread layered in garlic butter and served with a distinct marinara sauce.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"butter-garlic-bread.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 135.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"2f570d26-0adc-4f20-bcdb-9320a7bdf55e\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Mexican Fries\",\n" +
                    "\t\t\t\t\t\"Description\":\"You’ve never tried French Fries like these. These are topped with Mexican Chicken, Bell Peppers, Tomatoes served with Melted Chedder Sauce so you’ll keep coming back for more.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"mexican-fries.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 335.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,  \n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"4b99ccd3-bda4-4f65-9119-f3f335791c52\",\n" +
                    "\t\t\t\"CategoryName\": \"Soup of the Day\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"87553902-e4d2-4dff-b6d5-a2aa15fc28b5\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Corn Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our popular Chicken Corn Soup will lift your mood and spirit.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"piatto-special-soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 165.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e3d1326f-c0df-4829-9231-dac3170e82e2\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Hot & Sour Soup\",\n" +
                    "\t\t\t\t\t\"Description\":\"This unbeatable soup will heat things up, every spice lover’s ideal choice.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Hot-Sour-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 165.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"CategoryId\": \"46054194-da73-4af2-9aa9-ff705028cb1d\",\n" +
                    "\t\t\t\"CategoryName\": \"Sandwiches & Burgers\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"DishId\": \"e1a301c6-6780-4646-8707-2d01e3d61009\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Grilled Chicken Mushroom Burger\",\n" +
                    "\t\t\t\t\t\"Description\":\"If you love a mild taste then this is for you. With a grilled chicken breast piece that’s topped with seasoned mushrooms and Peri Peri sauce  and cheese.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Hot-Sour-Soup.jpg\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 165.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}, \n" +
                    "                {\n" +
                    "\t\t\t        \"DishId\": \"bdb91165-1abd-4a2e-8f68-05a99fba5911\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Peri Peri Chicken Burger\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our famous Peri Peri sauce will make this burger your favorite. It comes with a grilled chicken beast topped with Portuguese spices and seasoned with Peri Peri sauce and cheese.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"peri-peri-chicken-burger\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 435.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "           \t        \"DishId\": \"bb0b1312-ee6c-41a2-8d0a-518e80e7c532\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Jalapeno Grilled Chicken Burger\",\n" +
                    "\t\t\t\t\t\"Description\":\"This famouse burger is cooked to perfection. With grilled chicken served over lettuce tomatoes and jalapeno spread with chipotle sauce and melted cheese so you can savor every bite.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"burgers\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 435.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"8fc88782-fbd2-4a2b-91ab-b03a5130ab72\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Zinger Burger\",\n" +
                    "\t\t\t\t\t\"Description\":\"If you like it hot and spicy be sure to try our Zinger Burger. Made with a crispy chicken layered with lettuce and tomatoes dripped with melted cheddar cheese and Caesar sauce.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"Zinger-burger\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 355.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"f4defdca-c788-484a-8acf-f647a753c47c\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Piatto Club House Burger\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our trademark burger with grilled chicken is topped off with a fried egg, lettuce, tomatoes and slice of cheese served with our Piatto sauce.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"piatto-club-house-burger\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 395.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"69ad42e8-cce2-4de9-962a-9dcdd47cecbe\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Piatto Club Sandwich\",\n" +
                    "\t\t\t\t\t\"Description\":\"This favorite Triple Decker Sandwich will definitely fill you up layered with crispy fried chicken, fried egg, lettuce and tomato spread with chipotle mayonnaise.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"piatto-club-sandwich\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 395.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"fcbab19e-1735-45f2-ad11-22cee8f1dfa8\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Fajita Sandwich\",\n" +
                    "\t\t\t\t\t\"Description\":\"Crispy Fajita chicken served with Chipotle, lettuce, tomato and cheese served with house Piatto sauce.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"chicken-fajita-sandwiches\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 395.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"64bb55a5-90d2-4d74-b601-3a79a893ba3b\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Piatto Twister\",\n" +
                    "\t\t\t\t\t\"Description\":\"Crispy chicken along with cheese, crispy lettuce and chipotle garlic mayo sauce and wrap with our own pita bread, on side fries.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"No image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 295.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            \"CategoryId\": \"99419bb8-9cb9-43f6-8a3c-bf9edda2ed76\",\n" +
                    "\t\t\t\"CategoryName\": \"Salads\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "                    \"DishId\": \"55e07d7e-6b1a-4cf9-abe9-ff177c94fe82\",\n" +
                    "\t\t\t\t\t\"DishName\": \"SALADS\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our range of salad comes in a variety of taste and ingredients to match any craving\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"special-salad-02\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 455.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            \"CategoryId\": \"7e96ca63-00ea-4eec-8b77-d1bff573aedb\",\n" +
                    "\t\t\t\"CategoryName\": \"Chicken\",\n" +
                    "\t\t\t\"DishList\":[\n" +
                    "\t\t\t\t{\n" +
                    "                    \"DishId\": \"60fe4d45-9a6b-4ab6-a1df-931806bf9311\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Piatto Crispy Fried Broast\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our signature broast marinated with spices, coated with seasoned flour and cooked to a beautiful golden crisp. Served with dinner role and signature sauce\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"piatto-crispy-braost\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 295.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"7f577a32-ada0-4a0e-afdb-2073be99bb96\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Arabic Roasted Chicken\",\n" +
                    "\t\t\t\t\t\"Description\":\"This scrumptious roasted chicken dish seasoned with exotic Arabian spices is served with Moroccan sauce on the side.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"arabian-chicken\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 295.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"0727e408-5a6a-4693-866b-8173070098fc\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Peri Peri Chicken\",\n" +
                    "\t\t\t\t\t\"Description\":\"Our delicious grilled chicken moistened with Peri Peri sauce to give your taste buds that extra kick. Served with signature sauce.*make it a meaby adding a slideline and drink for Rs. 150\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"peri-peri-chicken\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 295.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }\n" +
                    "\t\t\t\t\t\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            \"CategoryId\": \"8302262b-cfe9-4ccc-9754-fac136c74b60\",\n" +
                    "\t\t\t\"CategoryName\": \"Main Course\",\n" +
                    "\t\t    \"DishList\":[\n" +
                    "                {\n" +
                    "                    \"DishId\": \"3e06b5ff-77e3-49e0-a6ee-26c8257f49b1\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken steak with mushroom sauce\",\n" +
                    "\t\t\t\t\t\"Description\":\"Grilled chicken steak with mushroom sauce served with Mexican rice and fries.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"No image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"358d61b8-0054-429d-90db-ae7d1b891f4c\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Steak with pepper sauce\",\n" +
                    "\t\t\t\t\t\"Description\":\"Grilled chicken steak topped with pepper sauce bed on grilled vegetable served with rice and fries.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"d3b6ae42-9210-4411-8132-209449e082e9\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Tarragon\",\n" +
                    "\t\t\t\t\t\"Description\":\"2 Pieces of delicious boneless grilled chicken breast marinated in special cream tarragon sauce, served with garlic bread, fries and veggies.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"2a1bb659-e3ab-4ad8-b88b-8112f2c58461\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Moroccan\",\n" +
                    "\t\t\t\t\t\"Description\":\"2 Pieces of delicious boneless grilled chicken marinated in special Moroccan red sauce, served with fries or Mexican rice and veggies.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"d05aea1a-a221-4339-810f-cf4ab53536c6\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Chicken Fajita\",\n" +
                    "\t\t\t\t\t\"Description\":\"(Served on a sizzler with rice & tortillas)\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "                }, \n" +
                    "                {\n" +
                    "    \t            \"DishId\": \"5fc0f8d9-3e4f-44f5-a8dc-c633a387b93e\",\n" +
                    "\t\t\t\t\t\"DishName\": \"Stuffed Chicken Alaska\",\n" +
                    "\t\t\t\t\t\"Description\":\"Chicken breast stuffed with mushroom, tomato, olives and cheese, serves over Alaska cream sauce on side with fries and rice.\",\n" +
                    "\t\t\t\t\t\"ImageURL\": \"no image\",\n" +
                    "\t\t\t\t\t\"TotalPrice\": 535.0,\n" +
                    "\t\t\t\t\t\"AvailabilityStatusId\": 1,\n" +
                    "\t\t\t\t\t\"Variants\":[]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t]\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
