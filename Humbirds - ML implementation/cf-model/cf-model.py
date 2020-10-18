import pandas as pd

ratings = pd.read_csv('C:\\Users\\Aritra Marik\\Desktop\\Humbirds - ML implementation\\cf-model\\top50.csv').drop(['Beats.Per.Minute','Energy','Danceability','Loudness..dB..','Liveness','Valence.','Length.','Acousticness..','Speechiness.'],axis=1)

ratings.head()

print(ratings)

##pivot on the dataset . row = dataframe of user id column = movie which they rated. values of each column= rating
user_ratings = ratings.pivot_table(index=['userID'],columns=['genre'],values='Popularity')

user_ratings.head()

##print(user_ratings)

##drop all movies which have less than ten users who have rated it

user_ratings = user_ratings.dropna(thresh=10,axis=1).fillna(0)

user_ratings.head()

##print(user_ratings)

##for similarity between items

item_similarity_df = user_ratings.corr(method='pearson')

item_similarity_df.head()

##print(item_similarity_df)

def get_similar_users(genre_name,user_rating):
    similar_score = item_similarity_df[genre_name]*(user_rating-50)
    similar_score = similar_score.sort_values(ascending=False)

    return similar_score


action_lov= [("dance pop",85),("reggaeton flow",45),("pop",92)]

similar_users = pd.DataFrame()

for genre,rating in action_lov:
    similar_users=similar_users.append(get_similar_users(genre,rating),ignore_index=True)

similar_users.head()

##print(similar_movies)

print(similar_users.sum().sort_values(ascending=False))