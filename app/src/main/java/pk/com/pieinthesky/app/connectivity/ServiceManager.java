package pk.com.pieinthesky.app.connectivity;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pk.com.pieinthesky.app.Configuration;
import pk.com.pieinthesky.app.LoginSelectionActivity;
import pk.com.pieinthesky.app.beans.AddressDetail;
import pk.com.pieinthesky.app.beans.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ServiceManager {

    private static Context context;
    static String token = "";

    //static String serviceURL ="http://192.168.80.111:1281";
    static String serviceURL = "http://www.pitsapi.com.asp1-101.phx1-1.websitetestlink.com";


    static String routePrefix = "/api/PITSMobileAPI/";


    public static void setContext(Context context) {
        ServiceManager.context = context;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ServiceManager.token = token;
    }

    private String ExecuteRequest(String request, String route) {
        String resp = "";
        HttpWeb webRequest = new HttpWeb();
        resp = webRequest.HttResponse(serviceURL + routePrefix + route, "POST", request, token);
        resp = checkResponse(resp);
        //if (!validateResponse(resp)) {
        //    resp = webRequest.HttResponse(serviceURL + routePrefix + route, "POST", request, token);
        //}
        return resp;
    }

    private String ExecuteLocalRequest(String request, String route) {
        String resp = "";
        HttpWeb webRequest = new HttpWeb();
        resp = webRequest.HttResponse("http://demo.movais.com/revakidemo/api/revaki/" + route, "POST", request, token);

        return resp;
    }

    private String checkResponse(String response) {
        JSONObject objres = null;
        try {
            if (response != "") {
                objres = new JSONObject(response);
                if (objres.has("StatusCode") && objres.getInt("StatusCode") == 301) {
                    response = "";

                    Intent intent = new Intent(context, LoginSelectionActivity.class);
                    intent.putExtra("IsInvalidToken", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }


    private boolean validateResponse(String response) {
        boolean status = true;
        JSONObject objres = null;
        try {
            if (response != "") {
                objres = new JSONObject(response);
                if (objres.has("StatusCode") && objres.getInt("StatusCode") == 301) {
                    User user = Configuration.getUser();
                    if (user.getLoginWith() == Configuration.AppLoginWith.User.value) {
                        Login(user.getLoginWith(), user.getUsername(), user.getPassword());
                    } else {
                        Login(user.getLoginWith(), user.getUniqueId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhotoURL());
                    }
                    status = false;
                }
            }
        } catch (JSONException e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    public JSONObject Register(String FirstName, String LastName, String Email, String Password) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("FirstName", FirstName);
            objreq.put("LastName", LastName);
            objreq.put("Email", Email);
            objreq.put("Password", Password);
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "register");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject Login(String LoginWith, String UniqueId, String FirstName, String LastName, String Email, String PhotoURL) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("LoginWith", LoginWith);
            objreq.put("UniqueId", UniqueId);
            objreq.put("FirstName", FirstName);
            objreq.put("LastName", LastName);
            objreq.put("Email", Email);
            objreq.put("PhotoURL", PhotoURL);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "login");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject Login(String LoginWith, String Username, String Password) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();

            objreq.put("LoginWith", LoginWith);
            objreq.put("Email", Username);
            objreq.put("Password", Password);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "login");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject ForgotPassword(String Email) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Email", Email);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "forgotpassword");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject VerifyOTP(String Email, String OTP) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Email", Email);
            objreq.put("OTP", OTP);
            //objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "verifyotp");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ResendOTP(String Email) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Email", Email);
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "resendotp");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject UserData(String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userdata");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PublicUserData(String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "publicuserdata");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject BusinessRating(String PlaceID, float Comfort, float Location, float Facilities, float Staff, float Valueformoney, String ReviewText) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceID);
            objreq.put("Comfort", Comfort);
            objreq.put("Location", Location);
            objreq.put("Facilities", Facilities);
            objreq.put("Staff", Staff);
            objreq.put("Valueformoney", Valueformoney);
            objreq.put("ReviewText", ReviewText);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "businessRating");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject PlaceRating(String PlaceID, float Rating, String ReviewText, JSONArray jaPhotos) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceID);
            objreq.put("Rating", Rating);
            objreq.put("ReviewText", ReviewText);
            objreq.put("Photos", jaPhotos);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "placeRatingPost");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PlaceReview(String PlaceID, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "placereview");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ReviewDetail(String ReviewID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("ReviewID", ReviewID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "reviewdetail");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject CommentsOnReview(String PlaceID, String ReviewId, String TextReview) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("ReviewId", ReviewId);
            objreq.put("TextReview", TextReview);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "commentsonreview");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject ReviewLike(String ReviewID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("ReviewID", ReviewID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "reviewlike");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject UserFollow(String UserID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserID", UserID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userfollow");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject HighRatingList() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "highRatingList");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject RestaurantList() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            //objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "restaurantlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ServiceList() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "ServiceList");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject CuisineList() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "CuisineList");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject SearchPlace(String SearchText, double Latitude, double Longitude, HashMap<String, List<String>> Filter, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {

            JSONObject objreq = new JSONObject();
            objreq.put("SearchText", SearchText);
            objreq.put("Latitude", Latitude);
            objreq.put("Longitude", Longitude);
            objreq.put("Filter", new JSONObject(getJSON(Filter)));
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "searchplace");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject UserTimeline(String UserID, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserID", UserID);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userdineline");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject UserReviews(String UserID, boolean WithCommentsList, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserID", UserID);
            objreq.put("WithCommentsList", WithCommentsList);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userReviewList");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PlaceDetail(String PlaceID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "PlaceDetail");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PlaceLike(String PlaceID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "placelike");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject PlaceVisited(String PlaceID, double Latitude, double Longitude) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("Latitude", Latitude);
            objreq.put("Longitude", Longitude);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "placevisited");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject AddUserCollection(String PlaceID) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceID", PlaceID);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addusercollection");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject UserCollection() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "usercollection");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject UpdateProfile(String UserId, String FirstName, String LastName, String Email, String MobileNo, double Latitude, double Longitude, String Location, String Description, String ProfilePhotoStream, String CoverPhotoStream) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("FirstName", FirstName);
            objreq.put("LastName", LastName);
            objreq.put("Email", Email);
            objreq.put("MobileNo", MobileNo);
            objreq.put("Latitude", Latitude);
            objreq.put("Longitude", Longitude);
            objreq.put("Location", Location);
            objreq.put("Description", Description);
            objreq.put("ProfilePhotoStream", ProfilePhotoStream);
            objreq.put("CoverPhotoStream", CoverPhotoStream);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "userDataUpdate");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject FilterPlace() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "filterPlace");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ReservationTableDays(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "reservationtabledays");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ReservationTableTimings(String PlaceId, String ReservationDate) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("ReservationDate", ReservationDate);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "reservationtabletimings");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject FetchReservation(String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "fetchreservation");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ReservationDetail(String ReservationId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("ReservationId", ReservationId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "reservationdetail");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject CancelReservation(String UserId, String ReservationId, String Reason) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("ReservationId", ReservationId);
            objreq.put("Reason", Reason);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "cancelreservation");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject Top10BusinessCollection() {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "top10businesscollection");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject BusinessCollection(int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {

            JSONObject objreq = new JSONObject();
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "businesscollection");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject PlaceListByBCollectionID(String CollectionMasterID, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {

            JSONObject objreq = new JSONObject();
            objreq.put("CollectionMasterID", CollectionMasterID);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "placeListByBCollectionID");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject AddTableReservation(String UserId, String PlaceId, String ReservationDate,
                                          String TimeSlotId, int NoOfGuest, String AdditionalRequests,
                                          String ByName, String Email, String ContactNo) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("PlaceId", PlaceId);
            objreq.put("ReservationDate", ReservationDate);
            objreq.put("TimeSlotId", TimeSlotId);
            objreq.put("NoOfGuest", NoOfGuest);
            objreq.put("AdditionalRequests", AdditionalRequests);
            objreq.put("ByName", ByName);
            objreq.put("Email", Email);
            objreq.put("ContactNo", ContactNo);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addtablereservation");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject MenuList(String PlaceId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            //objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "menulist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject QueueQRCodeDetail(String PlaceId, String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "qrcodedetail");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject QueueDetails(String UserId, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "queuedetails");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject QueueStatus(String QueueId, int TokenNo, String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("QueueId", QueueId);
            objreq.put("TokenNo", TokenNo);
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "queuestatus");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject AddInQueue(String PlaceId, String FullName, String NoOfPerson, String Instruction, String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("PlaceId", PlaceId);
            objreq.put("FullName", FullName);
            objreq.put("NoOfPerson", NoOfPerson);
            objreq.put("Instruction", Instruction);
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addinqueue");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject ChangeInQueue(String QueueId, int TokenNo, String ChangeType, int ChangeValue, String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("QueueId", QueueId);
            objreq.put("TokenNo", TokenNo);
            objreq.put("ChangeType", ChangeType);
            objreq.put("ChangeValue", ChangeValue);
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "changeinqueue");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject ActiveQueues(String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "activequeues");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject OrderList(String UserId, int Skip, int PageSize) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Skip", Skip);
            objreq.put("PageSize", PageSize);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "orderlist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject OrderDetail(String UserId, String OrderId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("OrderId", OrderId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "orderdetail");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject CreateOrder(String UserId, String OrderData) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("OrderData", new JSONObject(OrderData));
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "createorder");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject AddressList(String UserId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addresslist");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject AddAddress(String UserId, AddressDetail addressDetail) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("AddressDetail", new JSONObject(getJSON(addressDetail)));
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addaddress");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }


    public JSONObject DeleteAddress(String UserId, String AddressId) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("AddressId", AddressId);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "addressdel");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    public JSONObject RefreshFirebaseToken(String UserId, String FirebaseToken) {
        String reqjson = "";
        String resjson = "";
        JSONObject objres = null;
        try {
            JSONObject objreq = new JSONObject();
            objreq.put("UserId", UserId);
            objreq.put("FirebaseToken", FirebaseToken);
            objreq.put("Token", getToken());
            reqjson = objreq.toString();
            resjson = ExecuteRequest(reqjson, "refreshfirebasetoken");
            if (resjson != "") {
                objres = new JSONObject(resjson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objres;
    }

    private String getJSON(Object obj) {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(obj);
    }
}
