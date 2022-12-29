package com.example.android.mymall.Activities;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.mymall.Dac.MyRewardModel;
import com.example.android.mymall.Dac.ProductDetailsActivity;
import com.example.android.mymall.Dac.RewardAdapter;
import com.example.android.mymall.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartItemModelList;
    int lastPosition = -1;
    TextView cartTotalAmount;
    Boolean showDelete;
    private TextView footerText;
    private LinearLayout applyORremoveBtnContainer;
    private Button applyCoupanBtn,removeCoupanBtn;
    private String productOriginalPrice;

    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDelete) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDelete = showDelete;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTALAMOUNT;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                return new CartItemViewHolder(cartItemview);
            case CartItemModel.TOTALAMOUNT:
                View cartTotalAmountView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout,parent,false);
                return new CartTotalAmountViewHolder(cartTotalAmountView);
            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupenNo=cartItemModelList.get(position).getFreeCoupens();
                String price = cartItemModelList.get(position).getProductPrice();
                String cutPrice =cartItemModelList.get(position).getCuttedPrice();
                Long offerAppliedNo =cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                Long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean cod = cartItemModelList.get(position).isCod();

                ((CartItemViewHolder)holder).setCartItem(productId,resource,title,freeCoupenNo,price,cutPrice,offerAppliedNo,position,inStock,productQuantity,maxQuantity,qtyError,qtyIds,stockQty,cod);
                break;

            case CartItemModel.TOTALAMOUNT:
                int noOfItems = 0;
                int totalItemsAmount = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;


                for (int i = 0; i < cartItemModelList.size(); i++) {
                    if (cartItemModelList.get(i).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(i).isInStock()) {
                        int qty=Integer.parseInt(String.valueOf(cartItemModelList.get(i).getProductQuantity()));
                        noOfItems = noOfItems + qty;
                        if(TextUtils.isEmpty(cartItemModelList.get(i).getSelectedCouponId())) {
                            totalItemsAmount = totalItemsAmount + Integer.parseInt(cartItemModelList.get(i).getProductPrice())*qty;
                        }else {
                            totalItemsAmount = totalItemsAmount + Integer.parseInt(cartItemModelList.get(i).getDiscountedPrice())*qty;
                        }

                        if(!TextUtils.isEmpty(cartItemModelList.get(i).getCuttedPrice())){
                            savedAmount=savedAmount+(Integer.parseInt(cartItemModelList.get(i).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(i).getProductPrice()))*qty;
                            if(!TextUtils.isEmpty(cartItemModelList.get(i).getSelectedCouponId())) {
                                savedAmount=savedAmount+(Integer.parseInt(cartItemModelList.get(i).getProductPrice()) - Integer.parseInt(cartItemModelList.get(i).getDiscountedPrice()))*qty;
                            }
                        }else {
                            if(!TextUtils.isEmpty(cartItemModelList.get(i).getSelectedCouponId())) {
                                savedAmount=savedAmount+(Integer.parseInt(cartItemModelList.get(i).getProductPrice()) - Integer.parseInt(cartItemModelList.get(i).getDiscountedPrice()))*qty;
                            }
                        }

                    }
                }
                if (totalItemsAmount > 500){
                    deliveryPrice = "FREE";
                    totalAmount = totalItemsAmount;
                }else{
                    deliveryPrice = "60";
                    totalAmount = totalItemsAmount + 60;
                }
                cartItemModelList.get(position).setNoOfItems(noOfItems);
                cartItemModelList.get(position).setDeliveryAmount(deliveryPrice);
                cartItemModelList.get(position).setOverAllTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);

                ((CartTotalAmountViewHolder)holder).setCartTotalAmountView(noOfItems,totalItemsAmount,deliveryPrice,savedAmount,totalAmount);
                break;
            default:
                return;

        }
        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage ;
        private TextView  productTitle;
        private TextView  freeCoupens;
        private TextView  productPrice;
        private TextView  cuttedPrice;
        private TextView  productQuantity;
        private TextView  offersApplied;
        private TextView  coupensApplied;
        private ImageView freeCoupenIcon;
        private LinearLayout removeFromCart;
        private LinearLayout couponReedemLayout;
        private ImageView codImage;

        private RecyclerView rewardDialogRecyclerView;
        private LinearLayout miniRewardSelected;
        private TextView originalPrice;
        private TextView redemptionPrice;
        private TextView couponTitle;
        private TextView couponDate;
        private TextView couponContent;
        private TextView couponRedeemText;
        private TextView redeemBtn;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_product_image);
            productTitle = itemView.findViewById(R.id.cart_product_title);
            productPrice = itemView.findViewById(R.id.cart_product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            cuttedPrice = itemView.findViewById(R.id.cart_cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            freeCoupens = itemView.findViewById(R.id.coupon_text);
            freeCoupenIcon = itemView.findViewById(R.id.coupon_icon);
            removeFromCart = itemView.findViewById(R.id.remove_from_cart);
            couponReedemLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            redeemBtn = itemView.findViewById(R.id.redeem_btn);
            couponRedeemText = itemView.findViewById(R.id.coupen_redemption_text);
            codImage = itemView.findViewById(R.id.cod_indicator);

        }

        private  void setCartItem(final String productID, String resource, String title, Long freeCoupenNo, final String price, String cutPrice, Long offerAppliedNo, final int position, boolean inStock, final Long quantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty,boolean cod){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.smallholder)).into(productImage);
            productTitle.setText(title);


            final Dialog rewardsDialog = new Dialog(itemView.getContext());
            rewardsDialog.setContentView(R.layout.coupon_redeem_dialog);
            rewardsDialog.setCancelable(false);
            rewardsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            ImageView optionForRecycler = rewardsDialog.findViewById(R.id.icon_option);
            rewardDialogRecyclerView = rewardsDialog.findViewById(R.id.coupon_redeem_dialog_recycler_view);
            miniRewardSelected = rewardsDialog.findViewById(R.id.coupon_linear);
            originalPrice = rewardsDialog.findViewById(R.id.original_price);
            redemptionPrice = rewardsDialog.findViewById(R.id.after_redemption_price);
            couponTitle = rewardsDialog.findViewById(R.id.reward_text);
            couponDate = rewardsDialog.findViewById(R.id.reward_date);
            couponContent = rewardsDialog.findViewById(R.id.reward_content);
            applyCoupanBtn = rewardsDialog.findViewById(R.id.apply_coupon);
            removeCoupanBtn = rewardsDialog.findViewById(R.id.remove_coupon);
            applyORremoveBtnContainer = rewardsDialog.findViewById(R.id.remove_apply);
            footerText = rewardsDialog.findViewById(R.id.make_sure);

            footerText.setVisibility(View.GONE);
            applyORremoveBtnContainer.setVisibility(View.VISIBLE);



            LinearLayoutManager linearLayoutReward = new LinearLayoutManager(itemView.getContext());
            linearLayoutReward.setOrientation(RecyclerView.VERTICAL);
            rewardDialogRecyclerView.setLayoutManager(linearLayoutReward);

            originalPrice.setText(productPrice.getText());
            productOriginalPrice = price;
//                    productOriginalPrice = documentSnapshot.get("product_price").toString();
            RewardAdapter rewardAdapter = new RewardAdapter(position,DbQueries.myRewardModelList,true,rewardDialogRecyclerView,miniRewardSelected,productOriginalPrice,couponTitle,couponDate,couponContent,redemptionPrice,cartItemModelList);
            rewardDialogRecyclerView.setAdapter(rewardAdapter);
            rewardAdapter.notifyDataSetChanged();


            applyCoupanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (MyRewardModel rewardModel : DbQueries.myRewardModelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAlreadyUsed(true);
                                couponReedemLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_back));
                                couponRedeemText.setText(rewardModel.getCouponBody());
                                redeemBtn.setText("Coupon");
                            }
                        }
                        cartItemModelList.get(position).setDiscountedPrice(redemptionPrice.getText().toString().substring(3,redemptionPrice.getText().length()-2));
                        productPrice.setText(redemptionPrice.getText());
                        String offerDiscountedAmount = String.valueOf(Long.valueOf(price)-Long.valueOf(redemptionPrice.getText().toString().substring(3,redemptionPrice.getText().length()-2)));
                        coupensApplied.setVisibility(View.VISIBLE);
                        coupensApplied.setText("Coupon applied - Rs." + offerDiscountedAmount + "/-");
                        notifyItemChanged(cartItemModelList.size()-1);
                        rewardsDialog.dismiss();
                    }
                }
            });

            removeCoupanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(MyRewardModel rewardModel: DbQueries.myRewardModelList){
                        if(rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                            rewardModel.setAlreadyUsed(false);

                        }
                    }
                    couponTitle.setText("Coupon");
                    couponDate.setText("till xx-xx-xxxx");
                    couponContent.setText("Tap the icon on the top right corner to select your coupon.");
                    coupensApplied.setVisibility(View.INVISIBLE);
                    couponReedemLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.purple_500));
                    couponRedeemText.setText("Apply your coupon here.");
                    redeemBtn.setText("Redeem");
                    productPrice.setText("Rs."+price+"/-");
                    cartItemModelList.get(position).setSelectedCouponId(null);
                    notifyItemChanged(cartItemModelList.size()-1);
                    rewardsDialog.dismiss();
                }
            });

            if (cod){
                codImage.setVisibility(View.VISIBLE);
            }else {
                codImage.setVisibility(View.INVISIBLE);
            }




            optionForRecycler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showOptionForRecycler();
                }
            });




           if (inStock){
               if (freeCoupenNo > 0){
                   freeCoupens.setVisibility(View.VISIBLE);
                   freeCoupenIcon.setVisibility(View.VISIBLE);
                   if (freeCoupenNo == 1){
                       freeCoupens.setText("free "+ freeCoupenNo + " coupen");
                   }else{
                       freeCoupens.setText("free "+ freeCoupenNo + " coupens");
                   }
               } else {
                   freeCoupens.setVisibility(View.INVISIBLE);
                   freeCoupenIcon.setVisibility(View.INVISIBLE);
               }
               productPrice.setText("Rs."+price+"/-");
               productPrice.setTextColor(Color.parseColor("#000000"));
               cuttedPrice.setText("Rs."+cutPrice+"Rs.");
               couponReedemLayout.setVisibility(View.VISIBLE);

               if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                   for (MyRewardModel rewardModel : DbQueries.myRewardModelList) {
                       if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                           couponReedemLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_back));
                           couponRedeemText.setText(rewardModel.getCouponBody());
                           redeemBtn.setText("Coupan");

                           couponContent.setText(rewardModel.getCouponBody());
                           if(rewardModel.getType().equals("Discount")){
                               couponTitle.setText(rewardModel.getType());
                           }else {
                               couponTitle.setText("FLAT Rs."+rewardModel.getDiscount()+" OFF");
                           }
                           final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                           couponDate.setText("till " + simpleDateFormat.format(rewardModel));
                       }
                   }
                   productPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                   redemptionPrice.setText("Rs."+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                   String offerDiscountedAmount=String.valueOf(Long.valueOf(price)-Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                   coupensApplied.setVisibility(View.VISIBLE);
                   coupensApplied.setText("Coupan applied - Rs." + offerDiscountedAmount + "/-");
               }else {
                   coupensApplied.setVisibility(View.INVISIBLE);
                   couponReedemLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.purple_500));
                   couponRedeemText.setText("Apply your coupan here.");
                   redeemBtn.setText("Redeem");

               }


               productQuantity.setText("Qty: "+String.valueOf(quantity));
                if (!showDelete) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                }



               productQuantity.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Dialog quantityDialog = new Dialog(itemView.getContext());
                       quantityDialog.setContentView(R.layout.quantity_dialog);
                       quantityDialog.setCancelable(false);
                       quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                       EditText quantityNo = quantityDialog.findViewById(R.id.no_of_quantity);
                       Button cancelBtn = quantityDialog.findViewById(R.id.dialog_cancel_btn);
                       Button okBtn = quantityDialog.findViewById(R.id.dialog_ok_btn);
                       quantityNo.setHint("Max "+ String.valueOf(maxQuantity));



                       cancelBtn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               quantityDialog.dismiss();
                           }
                       });

                       okBtn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if (!TextUtils.isEmpty(quantityNo.getText())){
                                   if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {
                                       if(itemView.getContext() instanceof DashboardActivity) {
                                           cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));

                                       }else{
                                           if (DeliveryActivity.fromCart) {
                                               cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                           } else {
                                               DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                           }
                                       }


                                       productQuantity.setText("Qty: " + quantityNo.getText());
                                       notifyItemChanged(cartItemModelList.size() - 1);


                                       if(!showDelete){
                                           DeliveryActivity.loadingDialog.show();
                                           DeliveryActivity.cartItemModelList.get(position).setQtyError(false);

                                           int initialQty = Integer.parseInt(String.valueOf(quantity));
                                           int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                           final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

                                           if (finalQty > initialQty) {
                                               for (int y = 0; y < finalQty - initialQty; y++) {
                                                   final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                   Map<String, Object> timeStamp = new HashMap<>();
                                                   timeStamp.put("time", FieldValue.serverTimestamp());

                                                   final int finalY = y;
                                                   firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName)
                                                           .set(timeStamp)
                                                           .addOnCompleteListener(task -> {
                                                               if (task.isSuccessful()) {
                                                                   qtyIds.add(quantityDocumentName);
                                                                   if (finalY + 1 == finalQty - initialQty) {
                                                                       firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                               .addOnCompleteListener(task1 -> {
                                                                                   if (task1.isSuccessful()) {
                                                                                       List<String> serverQuantity = new ArrayList<>();

                                                                                       for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {
                                                                                           serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                       }
                                                                                       long availableQty = 0;
                                                                                       for (String qtyID : qtyIds) {
//                                                                                                  cartItemModelList.get(finalX).setQtyError(false);
                                                                                           if (!serverQuantity.contains(qtyID)) {

                                                                                               DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                               DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);
                                                                                               Toast.makeText(itemView.getContext(), "Sorry, Required amount of quantity is not available", Toast.LENGTH_SHORT).show();

                                                                                       }
//                                                                                              if (serverQuantity.size() >= cartItemModelList.get(finalX).getStockQuantity()){
//                                                                                                  firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).update("in_stock",false);
//
//                                                                                                  }
                                                                                           else {
                                                                                               availableQty++;
                                                                                           }

                                                                                       }
                                                                                       DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                   } else {
                                                                                       String error = task1.getException().getMessage();
                                                                                       Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                   }
                                                                                   DeliveryActivity.loadingDialog.dismiss();

                                                                               });

                                                                   }

                                                               }else {
                                                                   String error = task.getException().getMessage();
                                                                   Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                               }

                                                           });

                                               }
                                           }else if(initialQty > finalQty){
                                               for (int x=0;x<initialQty-finalQty;x++) {
                                                   final String qtyID = qtyIds.get(qtyIds.size()-1-x);

                                                   final int finalX = x;
                                                   firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyID).delete()
                                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {
                                                                   qtyIds.remove(qtyID);
                                                                   DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                   if(finalX+1 == initialQty-finalQty){
                                                                       DeliveryActivity.loadingDialog.dismiss();
                                                                   }
                                                               }
                                                           });
                                               }


                                           }

                                       }

                                   }else{
                                       Toast.makeText(itemView.getContext(), "Max Quantity :"+ String.valueOf(maxQuantity), Toast.LENGTH_SHORT).show();
                                   }
                               }
                               quantityDialog.dismiss();
                           }
                       });
                       quantityDialog.show();


                   }
               });

               if (offerAppliedNo > 0){
                   offersApplied.setVisibility(View.VISIBLE);
                   String offerDiscountedAmount=String.valueOf(Long.valueOf(cutPrice)-Long.valueOf(price));
                   offersApplied.setText("offer applied -Rs."+offerDiscountedAmount+"/-");
               }
               else{
                   offersApplied.setVisibility(View.INVISIBLE);
               }

           }else{
               productPrice.setText("Out Of Stock");
               productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.purple_500));
               cuttedPrice.setText("");
               couponReedemLayout.setVisibility(View.GONE);
               offersApplied.setVisibility(View.GONE);
               productQuantity.setVisibility(View.INVISIBLE);
               coupensApplied.setVisibility(View.GONE);
               freeCoupenIcon.setVisibility(View.INVISIBLE);
               freeCoupens.setVisibility(View.INVISIBLE);

           }



            if (showDelete){
                removeFromCart.setVisibility(View.VISIBLE);
            }else{
                removeFromCart.setVisibility(View.GONE);
            }
            removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (MyRewardModel rewardModel : DbQueries.myRewardModelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAlreadyUsed(false);

                            }
                        }
                    }

                    if(!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query =true;
                        DbQueries.removeFromCartList(position, itemView.getContext(),cartTotalAmount);
                    }

                }
            });

                        redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(MyRewardModel rewardModel: DbQueries.myRewardModelList){
                        if(rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                            rewardModel.setAlreadyUsed(false);

                        }
                    }
                    rewardsDialog.show();
                }
            });




        }
        private void showOptionForRecycler() {
            if (rewardDialogRecyclerView.getVisibility() == View.GONE){
                rewardDialogRecyclerView.setVisibility(View.VISIBLE);
                miniRewardSelected.setVisibility(View.GONE);
            }
            else{
                rewardDialogRecyclerView.setVisibility(View.GONE);
                miniRewardSelected.setVisibility(View.VISIBLE);
            }
        }

    }

    public class CartTotalAmountViewHolder extends RecyclerView.ViewHolder{
        TextView noOfItems;
        TextView totalItemsAmount;
        TextView deliveryAmount;
        TextView savedAmount;
        TextView overAllTotalAmount;
        RelativeLayout cartLayout;


        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            noOfItems = itemView.findViewById(R.id.priceNoOfItemsTv);
            totalItemsAmount = itemView.findViewById(R.id.priceNoOfItemsValue);
            deliveryAmount =itemView.findViewById(R.id.deliveryValue);
            savedAmount = itemView.findViewById(R.id.saving_amount);
            overAllTotalAmount = itemView.findViewById(R.id.totalAmountValue);
//            cartLayout = itemView.findViewById(R.id.cart_layout);
        }

        private void setCartTotalAmountView(int noOfItemsText, int totalItemsAmountText, String deliveryAmountText, int savedAmountText, int overAllTotalAmountText){

            noOfItems.setText("Price ("+noOfItemsText+" items)");
            totalItemsAmount.setText("Rs."+totalItemsAmountText+"/-");
            if(deliveryAmountText.equals("FREE")){
                deliveryAmount.setText(deliveryAmountText);
            }else {
                deliveryAmount.setText("Rs." + deliveryAmountText + "/-");
            }
            savedAmount.setText("You will save Rs." +savedAmountText +"/- on this order");
            overAllTotalAmount.setText("Rs."+overAllTotalAmountText+"/-");
            cartTotalAmount.setText("Rs."+overAllTotalAmountText+"/-");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if(totalItemsAmountText == 0){
                if (DeliveryActivity.fromCart){
                    cartItemModelList.remove(cartItemModelList.size() -1);
                    DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() -1);
                }
                if (showDelete){
                   cartItemModelList.remove(cartItemModelList.size() - 1);
                }
//                cartLayout.setVisibility(View.GONE);
                parent.setVisibility(View.GONE);
            }else {
//                cartLayout.setVisibility(View.VISIBLE);
                parent.setVisibility(View.VISIBLE);
            }



        }
    }

}
